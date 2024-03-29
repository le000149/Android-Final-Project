package org.algonquin.cst2355.finalproject.songsearch;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
import org.algonquin.cst2355.finalproject.songsearch.Song;

@Dao
public interface SongDAO {

    @Query("SELECT * FROM Song")
    List<Song> getAllSongDistinct();

    @Query("select * from Song where artist = :artist")
    List<Song> getSong(String artist);

    @Query("SELECT * FROM Song WHERE title = :title")
    Song getSongByTitle(String title);


    @Insert
    void saveSong(Song song);

    @Query("delete from Song where artist = :artist")
    void deleteSong(String artist);
    @Query("DELETE FROM Song")
    void deleteAll();
}
