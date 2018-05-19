package yorkdev.sample.stust.day5;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import yorkdev.sample.stust.R;


public class MyActionBarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//        getSupportActionBar().setDisplayHomeAsUpEnabled();
//        getSupportActionBar().setIcon();
//        getSupportActionBar().setLogo();
//        getSupportActionBar().setDisplayUseLogoEnabled();


        setContentView(R.layout.activity_with_actionbar);

        Toolbar toolbar = findViewById(R.id.toolbar);
//        toolbar.setLogo();
//        toolbar.setTitle();
        setSupportActionBar(toolbar);

//        getSupportActionBar().setDisplayHomeAsUpEnabled();
//        getSupportActionBar().setDisplayShowTitleEnabled();
//        toolbar.setNavigationIcon();

//        getSupportActionBar().setHomeAsUpIndicator();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.actionbar, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }
}
