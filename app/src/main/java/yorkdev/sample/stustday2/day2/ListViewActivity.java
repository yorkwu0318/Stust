package yorkdev.sample.stustday2.day2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import yorkdev.sample.stustday2.R;


public class ListViewActivity extends AppCompatActivity {

    private String[] data = {"AA", "BB", "CC"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list_view);

        ListView listView = findViewById(R.id.listView);

        MyAdapter adapter = new MyAdapter(data);
        listView.setAdapter(adapter);
    }

    class MyAdapter extends BaseAdapter {

        String[] items;

        public MyAdapter(String[] items) {
            this.items = items;
        }

        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public String getItem(int i) {
            return items[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_text, viewGroup, false);
            TextView textView = view.findViewById(R.id.textView);
            textView.setText(items[i]);
            return view;
        }
    }
}
