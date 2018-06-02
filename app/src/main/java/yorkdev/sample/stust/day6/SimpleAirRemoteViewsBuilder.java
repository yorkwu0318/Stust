package yorkdev.sample.stust.day6;


import android.widget.RemoteViews;

import yorkdev.sample.stust.AirQuality;
import yorkdev.sample.stust.MyApp;
import yorkdev.sample.stust.R;

public class SimpleAirRemoteViewsBuilder implements RemoteViewsBuilderFactory.RemoteViewsBuilder {

    private RemoteViews remoteViews;

    public SimpleAirRemoteViewsBuilder() {
        remoteViews = new RemoteViews(MyApp.getAppContext().getPackageName(), R.layout.my_widget);
    }

    @Override
    public RemoteViewsBuilderFactory.RemoteViewsBuilder setAirQuality(AirQuality airQuality) {
        remoteViews.setTextViewText(R.id.textView, airQuality.AQI);
        return this;
    }

    @Override
    public RemoteViews create() {
        return remoteViews;
    }
}
