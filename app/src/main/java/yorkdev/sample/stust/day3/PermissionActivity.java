package yorkdev.sample.stust.day3;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import yorkdev.sample.stust.AirQuality;
import yorkdev.sample.stust.R;
import yorkdev.sample.stust.day2.DataManager;


public class PermissionActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 12345;
    private RecyclerView recyclerView;

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
                    recyclerView.setAdapter(new MyAdapter(response.body()));
                }
            }

            @Override
            public void onFailure(Call<List<AirQuality>> call, Throwable t) {
                Log.e("111", t.getMessage());
            }
        });

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            onLocationPermissionAvailable();
            Log.i("111", "Permission Available");
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
            Log.i("111", "show explanation message");

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE);
        } else {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE);

        }

        Log.i("111", "onCreate");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("111", "onStop");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("111", "onPause");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("111", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("111", "onResume");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    onLocationPermissionAvailable();
                    Log.i("111", "Permission Available");

                } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
                    Log.i("111", "Permission Denied");
                } else {
                    Log.i("111", "Never Ask Again");

                }
        }
    }

    private void onLocationPermissionAvailable() {

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
