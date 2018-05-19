package yorkdev.sample.stust.day5.stack;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import yorkdev.sample.stust.R;

public class ResultActivity extends AppCompatActivity {
    public static final String KEY_NAME = "key_name";
    public static final String KEY_PASSWORD = "key_password";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_input);

        EditText nameEdit = findViewById(R.id.name);
        EditText pwdEdit = findViewById(R.id.pwd);
        Button button = findViewById(R.id.button);

        button.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.putExtra(KEY_NAME, nameEdit.getText().toString());
            intent.putExtra(KEY_PASSWORD, pwdEdit.getText().toString());
            setResult(RESULT_OK, intent);
            finish();
        });
    }
}
