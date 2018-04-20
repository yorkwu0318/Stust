package yorkdev.sample.stust.day2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import yorkdev.sample.stust.AirQuality;
import yorkdev.sample.stust.R;

public class HttpUrlConnectionActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        new Thread(new Runnable() {
            @Override
            public void run() {
                final String response = sendRequest();
                Log.i("111", "response="+response);

//                Gson gson = new Gson();
//                Type listType = new TypeToken<List<AirQuality>>(){}.getType();
//                List<AirQuality> list = gson.fromJson(response, listType);
//
//                updateRecyclerView(list);

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    List<AirQuality> list = new ArrayList<>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        AirQuality airQuality = new AirQuality();
                        airQuality.SiteName = jsonArray.getJSONObject(i).getString("SiteName");
                        airQuality.AQI = jsonArray.getJSONObject(i).getString("AQI");
                        list.add(airQuality);
                    }

                    updateRecyclerView(list);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void updateRecyclerView(final List<AirQuality> list) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recyclerView.setAdapter(new MyAdapter(list));
            }
        });

        // or ...

//        new Handler(Looper.getMainLooper()).post(new Runnable() {
//            @Override
//            public void run() {
//                recyclerView.setAdapter(new MyAdapter(list));
//            }
//        });
    }

    private String sendRequest() {
        String result = "";
        HttpURLConnection urlConnection = null;

        try {
            URL url = new URL("http://opendata2.epa.gov.tw/AQI.json");
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            result = inputStreamToString(in);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return result;
    }

    private String inputStreamToString(InputStream is) {
        String rLine;
        StringBuilder answer = new StringBuilder();

        InputStreamReader isr = new InputStreamReader(is);

        BufferedReader rd = new BufferedReader(isr);

        try {
            while ((rLine = rd.readLine()) != null) {
                answer.append(rLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return answer.toString();
    }


    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        List<AirQuality> list;

        public MyAdapter(List<AirQuality> list) {
            this.list = list;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_air_quality, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.textCity.setText(list.get(position).SiteName);
            holder.textAqi.setText(list.get(position).AQI);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView textCity;
            TextView textAqi;

            public MyViewHolder(View itemView) {
                super(itemView);

                textCity = itemView.findViewById(R.id.textCity);
                textAqi = itemView.findViewById(R.id.aqi);
            }
        }
    }
}
