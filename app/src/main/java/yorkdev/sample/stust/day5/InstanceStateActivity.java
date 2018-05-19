package yorkdev.sample.stust.day5;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import yorkdev.sample.stust.R;

public class InstanceStateActivity extends AppCompatActivity {
    private String KEY_BUNDLE_VALUE = "just_key";
    private int value = 20;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_button);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(view -> {
            value = 100;
        });


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }
}
