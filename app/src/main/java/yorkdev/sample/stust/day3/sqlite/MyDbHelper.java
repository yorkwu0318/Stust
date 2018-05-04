package yorkdev.sample.stust.day3.sqlite;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

import yorkdev.sample.stust.AirQuality;

public class MyDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "FeedReader.db";

    interface Column {
         String TABLE_NAME = "airQuality";
         String COLUMN_UPDATE_TIME = "updateTime";
         String COLUMN_SITE_NAME = "siteName";
         String COLUMN_SITE_ZONE = "siteZone";
         String COLUMN_AIR_AQI = "airAqi";
         String COLUMN_AIR_PM25 = "airPm25";
         String COLUMN_LATITUDE = "latitude";
         String COLUMN_LONGITUDE = "longitude";
    }

    interface SQL {
         String SQL_CREATE_ENTRIES =
                "CREATE TABLE "+ Column.TABLE_NAME +" (" +
                        BaseColumns._ID+ " INTEGER PRIMARY KEY," +
                        Column.COLUMN_SITE_NAME + " TEXT," +
                        Column.COLUMN_SITE_ZONE + " TEXT," +
                        Column.COLUMN_AIR_AQI + " TEXT," +
                        Column.COLUMN_AIR_PM25 + " TEXT," +
                        Column.COLUMN_LATITUDE + " TEXT," +
                        Column.COLUMN_LONGITUDE + " TEXT," +
                        Column.COLUMN_UPDATE_TIME + " TEXT)";

         String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + Column.TABLE_NAME;
    }

    public MyDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL.SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }

    public void update(List<AirQuality> list) {
        getWritableDatabase().delete(Column.TABLE_NAME, null, null);

        for (AirQuality airQuality : list) {
            ContentValues values = new ContentValues();
            values.put(Column.COLUMN_UPDATE_TIME, airQuality.PublishTime);
            values.put(Column.COLUMN_SITE_NAME, airQuality.SiteName);
            values.put(Column.COLUMN_SITE_ZONE, airQuality.County);
            values.put(Column.COLUMN_AIR_AQI, airQuality.AQI);
            values.put(Column.COLUMN_AIR_PM25, airQuality.PM25);
            values.put(Column.COLUMN_LATITUDE, airQuality.Latitude);
            values.put(Column.COLUMN_LONGITUDE, airQuality.Longitude);

            getWritableDatabase().insert(Column.TABLE_NAME, null, values);
        }
    }

    public List<AirQuality> getList() {
        String[] projection = {
                Column.COLUMN_UPDATE_TIME,
                Column.COLUMN_SITE_NAME,
                Column.COLUMN_SITE_ZONE,
                Column.COLUMN_AIR_AQI,
                Column.COLUMN_AIR_PM25,
                Column.COLUMN_LATITUDE,
                Column.COLUMN_LONGITUDE
        };

        Cursor cursor = getReadableDatabase().query(
                Column.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);

        List<AirQuality> list = new ArrayList<>();

        while (cursor.moveToNext()) {
            String updateTime = cursor.getString(cursor.getColumnIndexOrThrow(Column.COLUMN_UPDATE_TIME));
            String siteName = cursor.getString(cursor.getColumnIndexOrThrow(Column.COLUMN_SITE_NAME));
            String siteZone = cursor.getString(cursor.getColumnIndexOrThrow(Column.COLUMN_SITE_ZONE));
            String airPm25 = cursor.getString(cursor.getColumnIndexOrThrow(Column.COLUMN_AIR_PM25));
            String airAqi = cursor.getString(cursor.getColumnIndexOrThrow(Column.COLUMN_AIR_AQI));
            String latitude = cursor.getString(cursor.getColumnIndexOrThrow(Column.COLUMN_LATITUDE));
            String longitude = cursor.getString(cursor.getColumnIndexOrThrow(Column.COLUMN_LONGITUDE));

            AirQuality airQuality = new AirQuality();
            airQuality.AQI = airAqi;
            airQuality.SiteName = siteName;
            airQuality.PM25 = airPm25;
            airQuality.PublishTime = updateTime;
            airQuality.County = siteZone;
            airQuality.Latitude = latitude;
            airQuality.Longitude = longitude;

            list.add(airQuality);
        }


        cursor.close();

        return list;
    }
}
