package yorkdev.sample.stust.day6;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.gson.Gson;

import yorkdev.sample.stust.AirQuality;
import yorkdev.sample.stust.R;

import static android.content.Context.MODE_PRIVATE;


public class AirQualityWidgetProvider extends AppWidgetProvider {
    public static final String ACTION_UPDATE_DATA = "action_update_data";

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.i("111", "onReceive");

        if (ACTION_UPDATE_DATA.equals(intent.getAction())) {
            SharedPreferences sharedPreferences = context.getSharedPreferences("airQuality", MODE_PRIVATE);
            String text = sharedPreferences.getString("air", null);
            AirQuality airQuality = new Gson().fromJson(text, AirQuality.class);

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.my_widget);
            remoteViews.setTextViewText(R.id.textView, airQuality.AQI);

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

            int[] ids = appWidgetManager.getAppWidgetIds(new ComponentName(context, AirQualityWidgetProvider.class));
            appWidgetManager.updateAppWidget(ids, remoteViews);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.i("111", "onUpdate");
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        Log.i("111", "onAppWidgetOptionsChanged");
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        Log.i("111", "onDeleted");
    }

    @Override
    public void onEnabled(Context context) {
        Log.i("111", "onEnabled");
    }

    @Override
    public void onDisabled(Context context) {
        Log.i("111", "onDisabled");
    }
}
