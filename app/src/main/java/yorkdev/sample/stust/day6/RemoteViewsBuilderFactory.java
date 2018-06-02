package yorkdev.sample.stust.day6;


import android.appwidget.AppWidgetManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import yorkdev.sample.stust.AirQuality;

public class RemoteViewsBuilderFactory {

    public interface RemoteViewsBuilder {
        RemoteViewsBuilder setAirQuality(AirQuality airQuality);
        RemoteViews create();
    }

    public static RemoteViewsBuilder create(Bundle widgetOption) {

        int minWidth = widgetOption.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
        int minHeight = widgetOption.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT);

        int rows = getCellsForSize(minHeight);
        int columns = getCellsForSize(minWidth);

        Log.i("111", "rows="+rows+" columns="+columns);

        return null;
    }

    private static int getCellsForSize(int size) {
        return (int)(Math.ceil(size + 30d)/70d);
    }
}


