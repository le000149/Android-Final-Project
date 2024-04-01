package org.algonquin.cst2355.finalproject.sunrisesunset;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;
/**
 * Entity class representing a Location in the database.
 * This class is used by Room to create a table for storing locations with latitude and longitude values.
 */
@Entity
public class Location {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "latitude")
    private String latitude;

    @ColumnInfo(name = "longitude")
    private String longitude;

    /**
     * Default constructor for creating an instance of Location without initial values.
     * Mainly used by Room to instantiate Location objects.
     */
    public Location() {}

    /**
     * Constructs a Location with specified latitude and longitude.
     *
     * @param latitude The latitude of the location.
     * @param longitude The longitude of the location.
     */
    public Location(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Gets the unique identifier of the location.
     *
     * @return The unique id.
     */
    public int getId() { return id; }

    /**
     * Sets the unique identifier of the location.
     *
     * @param id The unique id to set.
     */
    public void setId(int id) { this.id = id; }

    /**
     * Gets the latitude of the location.
     *
     * @return The latitude value.
     */
    public String getLatitude() { return latitude; }

    /**
     * Sets the latitude of the location.
     *
     * @param latitude The latitude value to set.
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    /**
     * Gets the longitude of the location.
     *
     * @return The longitude value.
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * Sets the longitude of the location.
     *
     * @param longitude The longitude value to set.
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
