package yorkdev.sample.stust.day5.stack;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import yorkdev.sample.stust.R;

public class ActivityB extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_button);
        Button button = findViewById(R.id.button);
        button.setText("B");
        button.setOnClickListener(view -> {
            startActivity(new Intent(this, ActivityC.class));
        });
    }
}
