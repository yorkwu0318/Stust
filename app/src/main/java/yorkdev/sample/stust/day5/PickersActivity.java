package yorkdev.sample.stust.day5;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import java.util.Calendar;

import yorkdev.sample.stust.R;

public class PickersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pickers);

        Button timePickerButton = findViewById(R.id.timePickerBtn);
        Button datePickerButton = findViewById(R.id.datePickerBtn);

        Calendar calendar = Calendar.getInstance();

        timePickerButton.setOnClickListener(view -> {

        });

        datePickerButton.setOnClickListener(view -> {
        });
    }
}
