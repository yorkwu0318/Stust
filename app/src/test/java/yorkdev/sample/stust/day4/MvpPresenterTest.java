package yorkdev.sample.stust.day4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import yorkdev.sample.stust.AirQuality;
import yorkdev.sample.stust.day4.data.Repository;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MvpPresenterTest {

    MvpPresenter presenter;

    @Mock
    MvpView view;

    @Mock
    Repository repository;

    @Before
    public void setUp() throws Exception {
        presenter = new MvpPresenter(repository, view);
    }

    @Test
    public void onCreate() throws Exception {

        AirQuality airQuality = new AirQuality();
        airQuality.SiteName = "stust";
        airQuality.AQI = "100";

        List<AirQuality> list = new ArrayList<>();
        list.add(airQuality);

        doAnswer(invocation -> {
            ((Repository.Callback<List<AirQuality >>) invocation.getArguments()[0]).onSuccess(list);
            return null;
        }).when(repository).getAirQualityList(any());

        presenter.onCreate();
        verify(view).updateList(list);
    }

}