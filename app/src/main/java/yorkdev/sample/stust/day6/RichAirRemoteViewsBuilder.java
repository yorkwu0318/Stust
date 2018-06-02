package yorkdev.sample.stust.day6;


import android.widget.RemoteViews;

import yorkdev.sample.stust.AirQuality;
import yorkdev.sample.stust.MyApp;
import yorkdev.sample.stust.R;

public class RichAirRemoteViewsBuilder implements RemoteViewsBuilderFactory.RemoteViewsBuilder {

    private RemoteViews remoteViews;

    public RichAirRemoteViewsBuilder() {
        this.remoteViews = new RemoteViews(MyApp.getAppContext().getPackageName(), R.layout.my_rich_widget);
    }

    @Override
    public RemoteViewsBuilderFactory.RemoteViewsBuilder setAirQuality(AirQuality airQuality) {
        remoteViews.setTextViewText(R.id.aqi, airQuality.AQI);
        remoteViews.setTextViewText(R.id.textCity, airQuality.County);
        remoteViews.setTextViewText(R.id.textTime, airQuality.PublishTime);
        return this;
    }

    @Override
    public RemoteViews create() {
        return remoteViews;
    }
}
