package yorkdev.sample.stust.day4.data;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import yorkdev.sample.stust.day2.DataManager;

public class RemoteDataSource {
    public static DataManager generate() {
        return new Retrofit.Builder()
                .baseUrl("http://opendata2.epa.gov.tw/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(DataManager.class);
    }
}
