package yorkdev.sample.stust.day4;


import java.util.List;

import yorkdev.sample.stust.AirQuality;
import yorkdev.sample.stust.day4.data.Repository;

public class MvpPresenter {
    private Repository repository;
    private MvpView view;


    public MvpPresenter(Repository repository, MvpView view) {
        this.repository = repository;
        this.view = view;
    }

    public void onCreate() {

        repository.getAirQualityList(new Repository.Callback<List<AirQuality>>() {
            @Override
            public void onSuccess(List<AirQuality> list) {
                if (list == null || list.size() == 0) {
                    return;
                }

                view.updateList(list);
            }

            @Override
            public void onError(String message) {
                // error handle
            }

            @Override
            public void onFailed(String message) {
                // error handle
            }
        });
    }



}
