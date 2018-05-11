package yorkdev.sample.stust.day4.data;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Response;
import yorkdev.sample.stust.AirQuality;
import yorkdev.sample.stust.MyApp;

public class RepositoryImpl implements Repository {
    public static final String KEY_AIR_SHARED = "AirQuality";
    public static final String AIR_UPDATE_TIME = "air_update_time";

    @Override
    public void getAirQualityList(Callback<List<AirQuality>> callback) {

        if (isDbUpdate()) {
            getListFromDb(callback);
            return;
        }

        Call<List<AirQuality>> call = RemoteDataSource.generate().getList();
        call.enqueue(new retrofit2.Callback<List<AirQuality>>() {
            @Override
            public void onResponse(Call<List<AirQuality>> call, Response<List<AirQuality>> response) {
                if (response.isSuccessful()) {

                    List<AirQuality> list = response.body();
                    callback.onSuccess(list);

                    Executors.newSingleThreadExecutor().execute(() -> {
                        LocalDataSource.generate().insertAirQualities(list);
                        saveUpdateTime(list.get(0).PublishTime);
                    });
                } else if (hasDataInDb()) {
                    getListFromDb(callback);
                } else {
                    callback.onFailed(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<AirQuality>> call, Throwable t) {
                Log.e("111", t.getMessage());
                if (hasDataInDb()) {
                    getListFromDb(callback);
                } else {
                    callback.onError(t.getMessage());
                }
            }
        });
    }

    private boolean hasDataInDb() {
        return getUpdateTime() != null;
    }

    private boolean isDbUpdate() {
        if (!hasDataInDb()) return false;

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.TAIWAN);
        try {
            calendar.setTime(simpleDateFormat.parse(getUpdateTime()));
            Calendar savedTime = transformTime(calendar);
            Calendar currentTime = transformTime(Calendar.getInstance());

            Log.i("111", currentTime.getTime().toString());
            Log.i("111", savedTime.getTime().toString());

            if (!currentTime.after(savedTime)) {
                Log.i("111", "time is the same");
                return true;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }

    private Calendar transformTime(Calendar c) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH),
                c.get(Calendar.HOUR_OF_DAY),
                0,
                0);

        return calendar;
    }

    private void getListFromDb(Callback<List<AirQuality>> callback) {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<AirQuality> list = LocalDataSource.generate().getAll();
            new Handler(Looper.getMainLooper()).post(() -> callback.onSuccess(list));
        });

    }

    @Override
    public void saveUpdateTime(String updateTime) {
        SharedPreferences sharedPreferences = MyApp.getAppContext().getSharedPreferences(KEY_AIR_SHARED, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(AIR_UPDATE_TIME, updateTime).apply();
    }

    @Override
    public String getUpdateTime() {
        SharedPreferences sharedPreferences = MyApp.getAppContext().getSharedPreferences(KEY_AIR_SHARED, Context.MODE_PRIVATE);
        return sharedPreferences.getString(AIR_UPDATE_TIME, null);
    }
}
