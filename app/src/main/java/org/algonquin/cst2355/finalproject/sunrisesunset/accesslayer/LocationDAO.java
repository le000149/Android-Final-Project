package org.algonquin.cst2355.finalproject.sunrisesunset.accesslayer;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import org.algonquin.cst2355.finalproject.sunrisesunset.Location;
import org.algonquin.cst2355.finalproject.sunrisesunset.accesslayer.LocationDatabase;

import java.util.List;
@Dao
public interface LocationDAO {
    @Query("SELECT DISTINCT latitude FROM Location")
    List<String> getAllLatitudesDistinct();
    @Query("SELECT DISTINCT longitude FROM Location")
    List<String> getAllLogitudesDistinct();

    @Query("SELECT * FROM Location WHERE latitude = :latitude AND longitude = :longitude")
    List<Location> getLocations(String latitude, String longitude);

    @Insert
    void saveLocations(Location log);
    @Query("SELECT * FROM Location")
    List<Location> getAllLocations();
    @Insert
    void saveLocations(List<Location> locations);

    @Query("DELETE FROM Location WHERE latitude = :latitude AND longitude = :longitude")
    void deleteLocation(String latitude, String longitude);
}
