package yorkdev.sample.stust.day5;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import yorkdev.sample.stust.R;

public class SharedPreferenceActivity extends AppCompatActivity {
    private final static String KEY_SP = "key_sp";
    private final static String KEY_NAME = "key_name";
    private final static String KEY_PWD = "key_pwd";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_input);

        EditText nameEdit = findViewById(R.id.name);
        EditText pwdEdit = findViewById(R.id.pwd);
        Button button = findViewById(R.id.button);

        SharedPreferences sharedPreferences = getSharedPreferences(KEY_SP, MODE_PRIVATE);

        button.setOnClickListener(view -> {

            Toast.makeText(this, "傳送成功", Toast.LENGTH_SHORT).show();
        });

    }
}
