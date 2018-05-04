package yorkdev.sample.stust.day3;

import android.Manifest;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.Executors;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import yorkdev.sample.stust.AirQuality;
import yorkdev.sample.stust.R;
import yorkdev.sample.stust.day2.DataManager;
import yorkdev.sample.stust.day3.room.MyDatabase;
import yorkdev.sample.stust.day3.sqlite.MyDbHelper;

@RuntimePermissions
public class RoomActivity extends AppCompatActivity {
    public static final String KEY_AIR_SHARED = "AirQuality";
    public static final String AIR_UPDATE_TIME = "air_update_time";
    private RecyclerView recyclerView;
    private boolean hasUpdateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i("111", "onCreate");

        RoomActivityPermissionsDispatcher.onLocationAvailableWithPermissionCheck(this);

        setContentView(R.layout.activity_recycler_view);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        hasUpdateTime = getUpdateTime() != null;
        if (hasUpdateTime) {
            updateUiFromDb();
            return;
        }

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

                    recyclerView.setAdapter(new MyAdapter(list));
//                    MyDbHelper myDbHelper = new MyDbHelper(RoomActivity.this);
//                    myDbHelper.update(list);
                    Executors.newSingleThreadExecutor().execute(() -> {
                        MyDatabase.getInstance(RoomActivity.this).myDao().insertAirQualities(list);
                        saveUpdateTime(list.get(0).PublishTime);
                    });
                }
            }

            @Override
            public void onFailure(Call<List<AirQuality>> call, Throwable t) {
                Log.e("111", t.getMessage());
                if (hasUpdateTime) {
                    updateUiFromDb();
                }
            }
        });

    }

    private void updateUiFromDb() {
//        MyDbHelper myDbHelper = new MyDbHelper(this);
//        List<AirQuality> list = myDbHelper.getList();
        Executors.newSingleThreadExecutor().execute(() -> {
            List<AirQuality> list = MyDatabase.getInstance(this).myDao().getAll();

            new Handler(Looper.getMainLooper()).post(() -> recyclerView.setAdapter(new MyAdapter(list)));
        });


    }

    private void saveUpdateTime(String time) {
        SharedPreferences sharedPreferences = getSharedPreferences(KEY_AIR_SHARED, MODE_PRIVATE);
        sharedPreferences.edit().putString(AIR_UPDATE_TIME, time).apply();
    }

    private String getUpdateTime() {
        SharedPreferences sharedPreferences = getSharedPreferences(KEY_AIR_SHARED, MODE_PRIVATE);
        return sharedPreferences.getString(AIR_UPDATE_TIME, null);
    }

    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    public void onLocationAvailable() {
        Log.i("111", "Location Available");
    }

    @OnShowRationale(Manifest.permission.ACCESS_FINE_LOCATION)
    public void onLocationShowRationale(final PermissionRequest request) {
        Log.i("111", "showRationableForLocation");

        new AlertDialog.Builder(this)
                .setMessage("需要位置權限，自動取得最近的觀測站")
                .setPositiveButton("確認", (dialogInterface, i) -> request.proceed())
                .setNegativeButton("拒絕", (dialogInterface, i) -> request.cancel())
                .setCancelable(false)
                .show();
    }

    @OnPermissionDenied(Manifest.permission.ACCESS_FINE_LOCATION)
    public void OnLocationDenied() {
        Log.i("111", "Location Denied");
    }

    @OnNeverAskAgain(Manifest.permission.ACCESS_FINE_LOCATION)
    public void onLocationNeverAskAgain() {
        Log.i("111", "Never Ask Again");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        RoomActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        List<AirQuality> list;

        public MyAdapter(List<AirQuality> list) {
            this.list = list;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_air_quality, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.textCity.setText(list.get(position).SiteName);
            holder.textAqi.setText(list.get(position).AQI);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView textCity;
            TextView textAqi;

            public MyViewHolder(View itemView) {
                super(itemView);

                textCity = itemView.findViewById(R.id.textCity);
                textAqi = itemView.findViewById(R.id.aqi);
            }
        }
    }
}
