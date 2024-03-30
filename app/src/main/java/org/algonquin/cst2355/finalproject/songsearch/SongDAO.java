package org.algonquin.cst2355.finalproject.songsearch;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;


@Dao
public interface SongDAO {

    @Query("SELECT * FROM Song")
    List<Song> getAllSongDistinct();



    @Query("SELECT * FROM Song WHERE title = :title")
    Song getSongByTitle(String title);

    @Delete
    void delete(Song song);


    @Insert
    void saveSong(Song song);


    @Query("DELETE FROM Song")
    void deleteAll();
}
