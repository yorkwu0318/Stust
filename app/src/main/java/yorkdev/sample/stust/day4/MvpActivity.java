package yorkdev.sample.stust.day4;

import android.Manifest;
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
import android.widget.TextView;

import java.util.List;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;
import yorkdev.sample.stust.AirQuality;
import yorkdev.sample.stust.R;
import yorkdev.sample.stust.day4.data.RepositoryImpl;

@RuntimePermissions
public class MvpActivity extends AppCompatActivity implements MvpView {
    private RecyclerView recyclerView;


    private MvpPresenter presenter = new MvpPresenter(new RepositoryImpl(), this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i("111", "onCreate");

        MvpActivityPermissionsDispatcher.onLocationAvailableWithPermissionCheck(this);

        setContentView(R.layout.activity_recycler_view);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        presenter.onCreate();
    }



    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    public void onLocationAvailable() {
        Log.i("111", "Location Available");
    }

    @OnShowRationale(Manifest.permission.ACCESS_FINE_LOCATION)
    public void onLocationShowRationale(final PermissionRequest request) {
        Log.i("111", "showRationaleForLocation");

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

        MvpActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    public void updateList(List<AirQuality> list) {
        recyclerView.setAdapter(new MvpActivity.MyAdapter(list));
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
