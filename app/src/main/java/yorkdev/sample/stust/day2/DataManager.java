package yorkdev.sample.stust.day2;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import yorkdev.sample.stust.AirQuality;

public interface DataManager {
    @GET("AQI.json")
    Call<List<AirQuality>> getList();
}
