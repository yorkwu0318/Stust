package yorkdev.sample.stust.day4;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import yorkdev.sample.stust.AirQuality;
import yorkdev.sample.stust.R;
import yorkdev.sample.stust.databinding.ActivityMvvmBinding;

public class MvvmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AirQuality airQuality = new AirQuality();
        airQuality.SiteName = "Stust";
        airQuality.AQI = "125";

        ActivityMvvmBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_mvvm);
        binding.setAirQuality(airQuality);
    }
}
