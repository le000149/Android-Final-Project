package org.algonquin.cst2355.finalproject.sunrisesunset.accesslayer;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import org.algonquin.cst2355.finalproject.sunrisesunset.Location;
import org.algonquin.cst2355.finalproject.sunrisesunset.accesslayer.LocationDatabase;

import java.util.List;

/**
 * Data Access Object (DAO) for accessing location data from the Location table in the database.
 * Provides methods for inserting, querying, and deleting location records.
 */
@Dao
public interface LocationDAO {

    /**
     * Retrieves a list of distinct latitude values from the Location table.
     *
     * @return A list of strings representing distinct latitudes.
     */
    @Query("SELECT DISTINCT latitude FROM Location")
    List<String> getAllLatitudesDistinct();

    /**
     * Retrieves a list of distinct longitude values from the Location table.
     *
     * @return A list of strings representing distinct longitudes.
     */
    @Query("SELECT DISTINCT longitude FROM Location")
    List<String> getAllLogitudesDistinct();

    /**
     * Fetches a list of Location objects that match the specified latitude and longitude.
     *
     * @param latitude The latitude of the desired locations.
     * @param longitude The longitude of the desired locations.
     * @return A list of Location objects matching the specified latitude and longitude.
     */
    @Query("SELECT * FROM Location WHERE latitude = :latitude AND longitude = :longitude")
    List<Location> getLocations(String latitude, String longitude);

    /**
     * Inserts a single Location object into the database.
     *
     * @param log The Location object to be inserted.
     */
    @Insert
    void saveLocations(Location log);

    /**
     * Retrieves all Location records from the database.
     *
     * @return A list of all Location objects in the database.
     */
    @Query("SELECT * FROM Location")
    List<Location> getAllLocations();

    /**
     * Inserts a list of Location objects into the database.
     *
     * @param locations The list of Location objects to be inserted.
     */
    @Insert
    void saveLocations(List<Location> locations);

    /**
     * Deletes a specific location from the database matching the given latitude and longitude.
     *
     * @param latitude The latitude of the location to be deleted.
     * @param longitude The longitude of the location to be deleted.
     */
    @Query("DELETE FROM Location WHERE latitude = :latitude AND longitude = :longitude")
    void deleteLocation(String latitude, String longitude);
}
