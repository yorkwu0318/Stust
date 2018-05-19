package yorkdev.sample.stust.day5;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import yorkdev.sample.stust.R;

public class OpenFileActivity extends AppCompatActivity {
    private static final String FILE_NAME = "account.txt";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_input);

        EditText nameEdit = findViewById(R.id.name);
        EditText pwdEdit = findViewById(R.id.pwd);
        Button button = findViewById(R.id.button);



        button.setOnClickListener(view -> {
            Toast.makeText(this, "傳送成功", Toast.LENGTH_SHORT).show();
        });
    }

    private void writeFileData(String filename, String message){
        try {
            FileOutputStream fileOutputStream = openFileOutput(filename, MODE_PRIVATE);
            byte[]  bytes = message.getBytes();
            fileOutputStream.write(bytes);
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String readFileData(String fileName){
        try {
            FileInputStream fileInputStream = openFileInput(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        } catch (FileNotFoundException e) {
            return "";
        } catch (UnsupportedEncodingException e) {
            return "";
        } catch (IOException e) {
            return "";
        }
    }
}
