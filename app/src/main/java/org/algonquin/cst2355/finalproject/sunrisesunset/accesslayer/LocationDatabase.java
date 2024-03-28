package org.algonquin.cst2355.finalproject.sunrisesunset.accesslayer;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import org.algonquin.cst2355.finalproject.sunrisesunset.Location;


@Database(entities = {Location.class}, version = 1)
public abstract class LocationDatabase extends RoomDatabase {
    public static final String NAME = "LocationDatabase";

    public abstract LocationDAO LocationDao();
}
