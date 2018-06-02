package yorkdev.sample.stust.day6;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

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

@RuntimePermissions
public class PermissionDispatcherActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Location currentLocation;
    private MyAdapter myAdapter;
    private List<AirQuality> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

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
                    list = response.body();
                    myAdapter = new MyAdapter(list);
                    recyclerView.setAdapter(myAdapter);
                    findNearbySiteAndUpdateUi();
                }
            }

            @Override
            public void onFailure(Call<List<AirQuality>> call, Throwable t) {
                Log.e("111", t.getMessage());
            }
        });

        PermissionDispatcherActivityPermissionsDispatcher.onLocationAvailableWithPermissionCheck(this);

        Log.i("111", "onCreate");
    }

    @SuppressLint("MissingPermission")
    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    public void onLocationAvailable() {
        Log.i("111", "Location Available");
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        findNearbySiteAndUpdateUi();
    }

    private void findNearbySiteAndUpdateUi() {
        if (currentLocation != null && list != null) {

            float miniDistance = -1f;
            AirQuality nearByAirQuality = null;

            for (AirQuality airQuality : list) {
                Location location = new Location("AirQuality");
                location.setLongitude(Double.parseDouble(airQuality.Longitude));
                location.setLatitude(Double.parseDouble(airQuality.Latitude));

                float distance = currentLocation.distanceTo(location);
                if (distance < miniDistance || miniDistance == -1) {
                    miniDistance = distance;
                    nearByAirQuality = airQuality;
                }
            }

            String text = new Gson().toJson(nearByAirQuality);
            SharedPreferences sharedPreferences = getSharedPreferences("airQuality", MODE_PRIVATE);
            sharedPreferences.edit().putString("air", text).apply();

            Intent intent = new Intent(this, AirQualityWidgetProvider.class);
            intent.setAction(AirQualityWidgetProvider.ACTION_UPDATE_DATA);
            sendBroadcast(intent);

            myAdapter.setNearbyAirQuality(nearByAirQuality);
        }
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

        PermissionDispatcherActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        List<AirQuality> list;
        AirQuality airQuality;

        public MyAdapter(List<AirQuality> list) {
            this.list = list;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_air_quality_with_location, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.textCity.setText(list.get(position).SiteName);
            holder.textAqi.setText(list.get(position).AQI);
            holder.imageView.setVisibility(list.get(position) == airQuality ? View.VISIBLE : View.INVISIBLE);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public void setNearbyAirQuality(AirQuality airQuality) {
            int index = list.indexOf(airQuality);
            list.remove(airQuality);
            list.add(0, airQuality);
            notifyItemMoved(index, 0);
            this.airQuality = airQuality;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView textCity;
            TextView textAqi;
            ImageView imageView;

            public MyViewHolder(View itemView) {
                super(itemView);

                textCity = itemView.findViewById(R.id.textCity);
                textAqi = itemView.findViewById(R.id.aqi);
                imageView = itemView.findViewById(R.id.image);
            }
        }
    }
}
