package org.algonquin.cst2355.finalproject.songsearch;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import org.algonquin.cst2355.finalproject.songsearch.SongDAO;
@Database(entities = {Song.class}, version = 1)
public abstract class SongSearchDatabase extends RoomDatabase {
    public static final String NAME = "SongSearchDatabase";

    public abstract SongDAO SongDao();
}
