package algonquin.cst2335.finalproject;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SongDAO {
    @Insert
    long insertSong(Song song);

    @Query("SELECT * FROM songs")
    List<Song> getAllSongs();

    @Query("DELETE FROM songs")
    void deleteAllSongs(); // Changed to not accept a parameter

    @Delete
    void deleteSong(Song song); // Make sure this is correctly annotated if needed
}

