package org.algonquin.cst2355.finalproject;

import android.app.Application;

import androidx.room.Room;

import org.algonquin.cst2355.finalproject.dictionary.db.DictionaryDatabase;

public class MainApplication extends Application {

    private static DictionaryDatabase dictionaryDB;

    public static DictionaryDatabase getDictionaryDB() {
        return dictionaryDB;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // Create room database
        dictionaryDB = Room.databaseBuilder(getApplicationContext(), DictionaryDatabase.class, DictionaryDatabase.NAME).build();
    }

}
