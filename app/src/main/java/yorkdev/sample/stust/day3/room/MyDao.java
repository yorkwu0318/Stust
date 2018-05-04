package yorkdev.sample.stust.day3.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import yorkdev.sample.stust.AirQuality;

@Dao
public interface MyDao {

    @Query("SELECT * FROM AirQuality")
    List<AirQuality> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAirQualities(List<AirQuality> airQualities);
}
