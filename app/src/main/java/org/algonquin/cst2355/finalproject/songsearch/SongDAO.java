package org.algonquin.cst2355.finalproject.songsearch;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
import org.algonquin.cst2355.finalproject.songsearch.Song;

@Dao
public interface SongDAO {

    @Query("SELECT * FROM Song WHERE id IN (SELECT MIN(id) FROM Song GROUP BY word) ORDER BY id DESC")
    List<Song> getAllSongDistinct();

    @Query("select * from Song where word = :word")
    List<Song> getSong(String word);

    @Insert
    void saveSong(List<Song> songs);

    @Query("delete from Song where word = :word")
    void deleteSong(String word);

}
