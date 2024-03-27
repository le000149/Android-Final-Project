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

    @Insert
    void saveSong(List<Song> songs);

    @Query("delete from Song where artist = :artist")
    void deleteSong(String artist);

}
