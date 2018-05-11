package yorkdev.sample.stust.day4;


import java.util.List;

import yorkdev.sample.stust.AirQuality;

public interface MvpView {
    void updateList(List<AirQuality> list);
}
