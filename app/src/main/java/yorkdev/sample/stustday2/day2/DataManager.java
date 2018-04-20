package yorkdev.sample.stustday2.day2;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import yorkdev.sample.stustday2.AirQuality;

public interface DataManager {
    @GET("AQI.json")
    Call<List<AirQuality>> getList();
}
