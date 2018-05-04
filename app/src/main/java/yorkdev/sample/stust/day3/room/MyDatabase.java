package yorkdev.sample.stust.day3.room;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import yorkdev.sample.stust.AirQuality;

@Database(entities = {AirQuality.class}, version = 1)
public abstract class MyDatabase extends RoomDatabase {
    public abstract MyDao myDao();

    private static MyDatabase instance;

    public static MyDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    MyDatabase.class,
                    "AirQuality")
                    .build();
        }

        return instance;
    }
}
