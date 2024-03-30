package org.algonquin.cst2355.finalproject.sunrisesunset.accesslayer;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import org.algonquin.cst2355.finalproject.sunrisesunset.Location;


/**
 * Abstract database class for the application, extending RoomDatabase.
 * This database holds the Location entities and provides access to them through the LocationDAO.
 *
 * The database is a singleton, ensuring a single instance is used throughout the application
 * to avoid conflicts and data integrity issues.
 *
 * @version 1 The version number of the database, used to manage database migrations.
 */
@Database(entities = {Location.class}, version = 1)
public abstract class LocationDatabase extends RoomDatabase {
    /**
     * Name of the database. This is used internally to identify the database file on the device.
     */
    public static final String NAME = "LocationDatabase";

    /**
     * Provides the DAO for accessing location data. This method acts as an access point
     * for the application to perform CRUD operations on the Location entities within the database.
     *
     * @return The LocationDAO implementation provided by Room for accessing the database.
     */
    public abstract LocationDAO LocationDao();
}