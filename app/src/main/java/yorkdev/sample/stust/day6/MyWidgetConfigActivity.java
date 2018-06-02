package yorkdev.sample.stust.day6;


import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import yorkdev.sample.stust.AirQuality;
import yorkdev.sample.stust.R;

public class MyWidgetConfigActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_config_widget);

        EditText editText = findViewById(R.id.textCity);


        Button button = findViewById(R.id.button);
        button.setOnClickListener(view -> {
            SharedPreferences sharedPreferences = getSharedPreferences("airQuality", MODE_PRIVATE);
            sharedPreferences.edit().putString("home", editText.getText().toString()).apply();

            int widgetId = getIntent().getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            Intent intent = new Intent();
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
            setResult(RESULT_OK, intent);
            finish();
        });
    }
}
