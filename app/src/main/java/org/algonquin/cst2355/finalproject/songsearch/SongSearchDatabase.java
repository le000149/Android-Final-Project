package org.algonquin.cst2355.finalproject.songsearch;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import org.algonquin.cst2355.finalproject.songsearch.SongDAO;
/**
 * This class represents the Room database for storing song data.
 * Author: Hoang Anh Nguyen - 041099695
 */
@Database(entities = {Song.class}, version = 1)
public abstract class SongSearchDatabase extends RoomDatabase {

    /**
     * The name of the database.
     */
    public static final String NAME = "SongSearchDatabase";
    /**
     * Returns the Data Access Object (DAO) for interacting with the database.
     * @return The SongDAO object.
     */
    public abstract SongDAO SongDao();
}
