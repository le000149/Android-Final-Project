package org.algonquin.cst2355.finalproject.songsearch;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * This interface defines data access methods for interacting with the Song table in the Room database.
 * Author: Hoang Anh Nguyen - 041099695
 * Lab Section - 013
 */
@Dao
public interface SongDAO {
    /**
     * Retrieves all distinct songs from the database.
     * @return A list of distinct songs.
     */
    @Query("SELECT * FROM Song")
    List<Song> getAllSongDistinct();


    /**
     * Retrieves a song from the database based on its title.
     * @param title The title of the song to retrieve.
     * @return The Song object matching the title, or null if not found.
     */
    @Query("SELECT * FROM Song WHERE title = :title")
    Song getSongByTitle(String title);
    /**
     * Deletes a song from the database.
     * @param song The Song object to delete.
     */
    @Delete
    void delete(Song song);

    /**
     * Saves a song to the database.
     * @param song The Song object to save.
     */
    @Insert
    void saveSong(Song song);

    /**
     * Deletes all songs from the database.
     */
    @Query("DELETE FROM Song")
    void deleteAll();
}
