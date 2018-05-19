package yorkdev.sample.stust;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import yorkdev.sample.stust.day2.AnrActivity;
import yorkdev.sample.stust.day2.HttpUrlConnectionActivity;
import yorkdev.sample.stust.day2.ListViewActivity;
import yorkdev.sample.stust.day2.RecyclerViewActivity;
import yorkdev.sample.stust.day2.RetrofitActivity;
import yorkdev.sample.stust.day3.PermissionActivity;
import yorkdev.sample.stust.day3.PermissionDispatcherActivity;
import yorkdev.sample.stust.day3.RoomActivity;
import yorkdev.sample.stust.day3.SQLiteActivity;
import yorkdev.sample.stust.day4.MvpActivity;
import yorkdev.sample.stust.day4.MvvmActivity;
import yorkdev.sample.stust.day5.MyActionBarActivity;
import yorkdev.sample.stust.day5.OpenFileActivity;
import yorkdev.sample.stust.day5.PickersActivity;
import yorkdev.sample.stust.day5.SharedPreferenceActivity;
import yorkdev.sample.stust.day5.stack.ActivityA;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String[] options = {
                "List View",
                "RecyclerView",
                "ANR",
                "HttpUrlConnection",
                "Retrofit",
                "Official Permission Setting",
                "PermissionDispatcher Setting",
                "SQLite",
                "Room",
                "MVP",
                "Mvvm",
                "Action Bar",
                "ActivityStack",
                "Pickers",
                "SharedActivity",
                "Open File"
        };

        final Class[] classes = {
                ListViewActivity.class,
                RecyclerViewActivity.class,
                AnrActivity.class,
                HttpUrlConnectionActivity.class,
                RetrofitActivity.class,
                PermissionActivity.class,
                PermissionDispatcherActivity.class,
                SQLiteActivity.class,
                RoomActivity.class,
                MvpActivity.class,
                MvvmActivity.class,
                MyActionBarActivity.class,
                ActivityA.class,
                PickersActivity.class,
                SharedPreferenceActivity.class,
                OpenFileActivity.class
        };

        setContentView(R.layout.activity_recycler_view);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(new MyAdapter(Arrays.asList(options), new MyAdapterOnClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(MainActivity.this, classes[position]);
                startActivity(intent);
            }
        }));
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        private List<String> list;
        private MyAdapterOnClickListener listener;

        public MyAdapter(List<String> list, MyAdapterOnClickListener listener) {
            this.list = list;
            this.listener = listener;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_text, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(holder.getAdapterPosition());
                }
            });

            holder.textView.setText(list.get(position));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView textView;

            public MyViewHolder(View itemView) {
                super(itemView);

                textView = itemView.findViewById(R.id.textView);
            }
        }
    }

    interface MyAdapterOnClickListener {
        void onClick(int position);
    }
}
