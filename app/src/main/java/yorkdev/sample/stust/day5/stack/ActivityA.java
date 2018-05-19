package yorkdev.sample.stust.day5.stack;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import yorkdev.sample.stust.R;

public class ActivityA extends AppCompatActivity {
    private static final int REQUEST_CODE = 100;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_button);

        Button button = findViewById(R.id.button);
        button.setText("A");
        button.setOnClickListener(view -> {

            startActivity(new Intent(this, ActivityB.class));

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}
