package yorkdev.sample.stust.day4.data;


import java.util.List;

import yorkdev.sample.stust.AirQuality;

public interface Repository {
    interface Callback<T> {
        void onSuccess(T response);
        void onError(String message);
        void onFailed(String message);
    }

    void getAirQualityList(Callback<List<AirQuality>> callback);
    void saveUpdateTime(String updateTime);
    String getUpdateTime();
}
