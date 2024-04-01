package org.algonquin.cst2355.finalproject;

import android.app.Application;

import androidx.room.Room;

import org.algonquin.cst2355.finalproject.dictionary.db.DictionaryDatabase;
import org.algonquin.cst2355.finalproject.sunrisesunset.accesslayer.LocationDatabase;

public class MainApplication extends Application {

    private static DictionaryDatabase dictionaryDB;
    private static LocationDatabase locationDB;

    public static DictionaryDatabase getDictionaryDB() {
        return dictionaryDB;
    }
    public static LocationDatabase getLocationDB() {
        return locationDB;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // Create room database
        dictionaryDB = Room.databaseBuilder(getApplicationContext(), DictionaryDatabase.class, DictionaryDatabase.NAME).build();
        locationDB= Room.databaseBuilder(getApplicationContext(),LocationDatabase.class,LocationDatabase.NAME).build();
    }

}
