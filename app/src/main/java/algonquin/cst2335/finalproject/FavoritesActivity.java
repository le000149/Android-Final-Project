package algonquin.cst2335.finalproject;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class FavoritesActivity extends AppCompatActivity implements SongAdapter.OnSongInteractionListener {
    private RecyclerView favoritesRecyclerView;
    private SongAdapter favoritesAdapter;
    private SongDatabase songDatabase;
    private List<Song> backedUpSongs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        favoritesRecyclerView = findViewById(R.id.favoritesRecyclerView);
        favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the database
        songDatabase = Room.databaseBuilder(getApplicationContext(), SongDatabase.class, "song-db").build();

        loadFavoriteSongs();
    }

    private void loadFavoriteSongs() {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            List<Song> favoriteSongs = songDatabase.songDao().getAllSongs();
            runOnUiThread(() -> {
                favoritesAdapter = new SongAdapter(favoriteSongs, this);
                favoritesAdapter.setOnSongInteractionListener(this);
                favoritesRecyclerView.setAdapter(favoritesAdapter);
            });
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_favorites, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.delete_all) {
            confirmDeletion();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void confirmDeletion() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.confirm_deletion)
                .setMessage(R.string.are_you_sure_delete_all_favorite_songs)
                .setPositiveButton(R.string.delete, (dialog, which) -> deleteAllSongs())
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void deleteAllSongs() {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            // Backup the current list of songs before deleting
            backedUpSongs = favoritesAdapter.getAllSongs();

            // Delete all songs from the database
            songDatabase.songDao().deleteAllSongs();

            // Perform UI actions after deletion
            runOnUiThread(() -> {
                favoritesAdapter.setSongs(null); // Clear the adapter's data set
                // Show a message or perform any other UI action after deletion
            });
        });
    }

    @Override
    public void onSongDelete(Song song) {
        confirmDelSong(song);
    }

    private void confirmDelSong(Song song) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.confirm_deletion)
                .setMessage(getString(R.string.are_you_sure_delete_song, song.getTitle()))
                .setPositiveButton(R.string.delete, (dialog, which) -> deleteSong(song))
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void deleteSong(Song song) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            // Delete the song from the database
            songDatabase.songDao().deleteSong(song);

            // Perform UI actions after deletion
            runOnUiThread(() -> {
                favoritesAdapter.removeSong(song);
                // Show a message or perform any other UI action after deletion
            });
        });
    }

    @Override
    public void onSongClick(Song song) {
        // Handle click on a song item, e.g., show details of the song
    }
}
