package yorkdev.sample.stust.day6;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.gson.Gson;

import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import yorkdev.sample.stust.AirQuality;
import yorkdev.sample.stust.MyApp;
import yorkdev.sample.stust.day2.DataManager;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.MODE_PRIVATE;


public class AirQualityWidgetProvider extends AppWidgetProvider {
    public static final String ACTION_UPDATE_DATA = "action_update_data";
    public static final String ACTION_REFRESH = "action_refresh";

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.i("111", "onReceive");

        if (ACTION_UPDATE_DATA.equals(intent.getAction())) {
            AirQuality airQuality = getCurrentAirQuality(context);

            updateAllWidgets(airQuality);

        } else if (ACTION_REFRESH.equals(intent.getAction())) {

            onUpdate(context, AppWidgetManager.getInstance(context), AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, AirQualityWidgetProvider.class)));

        }
    }

    private void setCurrentAirQuality(Context context, AirQuality airQuality) {
        String text = new Gson().toJson(airQuality);

        SharedPreferences sharedPreferences = context.getSharedPreferences("airQuality", MODE_PRIVATE);
        sharedPreferences.edit().putString("air", text).apply();
    }


    private AirQuality getCurrentAirQuality(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("airQuality", MODE_PRIVATE);
        String text = sharedPreferences.getString("air", null);
        return new Gson().fromJson(text, AirQuality.class);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.i("111", "onUpdate");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://opendata2.epa.gov.tw/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        DataManager dataManager = retrofit.create(DataManager.class);
        Call<List<AirQuality>> call = dataManager.getList();
        call.enqueue(new Callback<List<AirQuality>>() {
            @Override
            public void onResponse(Call<List<AirQuality>> call, Response<List<AirQuality>> response) {
                if (response.isSuccessful()) {
                    List<AirQuality> list = response.body();
                    if (list == null || list.size() == 0) {
                        return;
                    }

                    setCurrentAirQuality(context, list.get(0));

                    AirQuality airQuality = getCurrentAirQuality(context);
                    updateAllWidgets(airQuality);

                }
            }

            @Override
            public void onFailure(Call<List<AirQuality>> call, Throwable t) {
                Log.e("111", t.getMessage());
            }
        });


    }

    private void updateAllWidgets(AirQuality airQuality) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(MyApp.getAppContext());
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(MyApp.getAppContext(), AirQualityWidgetProvider.class));

        for (int id : appWidgetIds) {
            Bundle widgetOption = appWidgetManager.getAppWidgetOptions(id);

            RemoteViews remoteViews = RemoteViewsBuilderFactory.create(widgetOption)
                    .setAirQuality(airQuality)
                    .create();

            appWidgetManager.updateAppWidget(id, remoteViews);
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 15);
        calendar.add(Calendar.HOUR, 1);

        AlarmManager alarmManager = (AlarmManager) MyApp.getAppContext().getSystemService(ALARM_SERVICE);
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC, calendar.getTimeInMillis(), createClockTickIntent());

    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        Log.i("111", "onAppWidgetOptionsChanged");

        RemoteViews remoteViews = RemoteViewsBuilderFactory.create(newOptions)
                .setAirQuality(getCurrentAirQuality(context))
                .create();

        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        Log.i("111", "onDeleted");
    }

    @Override
    public void onEnabled(Context context) {
        Log.i("111", "onEnabled");
    }

    @Override
    public void onDisabled(Context context) {
        Log.i("111", "onDisabled");

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        alarmManager.cancel(createClockTickIntent());
    }

    private PendingIntent createClockTickIntent() {
        Intent intent = new Intent(MyApp.getAppContext(), AirQualityWidgetProvider.class);
        return PendingIntent.getBroadcast(MyApp.getAppContext(), 0, intent, 0);
    }
}
