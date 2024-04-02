package org.algonquin.cst2355.finalproject.songsearch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.algonquin.cst2355.finalproject.R;

import java.util.List;
import java.util.concurrent.Executors;

/**
 * This activity displays a list of saved songs and allows the user to delete individual songs or all saved songs.
 * Author: Hoang Anh Nguyen - 041099695
 * Lab Section - 013
 */
public class SavedSongActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SongAdapter songAdapter;
    private SongDAO songDAO;
    /**
     * Overrides the onCreateOptionsMenu method to inflate the menu resource file.
     * @param menu The menu to be displayed in the Toolbar.
     * @return true if the menu is inflated successfully, false otherwise.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_saved_song, menu);
        return true;
    }
    /**
     * Overrides the onOptionsItemSelected method to handle menu item clicks.
     * @param item The menu item that was clicked.
     * @return true if the menu item click is handled, false otherwise.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_delete_all) {
            deleteAllSavedSongs();
            return true;
        }else if(item.getItemId() == R.id.AboutSongSavePage){
            showHelp();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    /**
     * Displays a help dialog explaining the functionality of the activity.
     */
    private void showHelp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.songSaveList));
        builder.setMessage(getString(R.string.songSaveListD));
        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Dismiss the dialog
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    /**
     * Deletes all saved songs from the database.
     */
    private void deleteAllSavedSongs() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.songDelete))
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Get the count of saved songs before deletion
                        int itemCountBeforeDeletion = songAdapter.getItemCount();

                        // User clicked OK button, delete all saved songs
                        Executors.newSingleThreadExecutor().execute(new Runnable() {
                            @Override
                            public void run() {
                                // Delete all saved songs from the database
                                songDAO.deleteAll();

                                // Refresh the RecyclerView to reflect the changes
                                List<Song> savedSongs = songDAO.getAllSongDistinct();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        songAdapter.setSongs(savedSongs);

                                        // Calculate the count of deleted items
                                        int deletedItemCount = itemCountBeforeDeletion - savedSongs.size();

                                        // Show a Snackbar indicating the number of items deleted
                                        Snackbar.make(findViewById(android.R.id.content),
                                                String.format("%d "+getString(R.string.songDeletemsg), deletedItemCount),
                                                Snackbar.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                })
                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog, do nothing
                    }
                });
        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Overrides the onCreate method to initialize the activity.
     * @param savedInstanceState The saved instance state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_song);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.saved_song_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        songAdapter = new SongAdapter();
        recyclerView.setAdapter(songAdapter);

        // Initialize Room database asynchronously
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                // Initialize Room database
                SongSearchDatabase songDatabase = Room.databaseBuilder(getApplicationContext(),
                        SongSearchDatabase.class, "SongSearchDatabase").build();
                songDAO = songDatabase.SongDao(); // Initialize the DAO

                // Retrieve list of saved songs and update RecyclerView
                List<Song> savedSongs = songDAO.getAllSongDistinct();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        songAdapter.setSongs(savedSongs);
                    }
                });
            }
        });
    }
    /**
     * Adapter class for the RecyclerView to display saved songs.
     */
    public  class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {
        private List<Song> songs;
        /**
         * Sets the list of songs to be displayed in the RecyclerView.
         * @param songs The list of songs to be displayed.
         */
        public void setSongs(List<Song> songs) {
            this.songs = songs;
            notifyDataSetChanged();
        }
        /**
         * Overrides the onCreateViewHolder method to create ViewHolder objects.
         * @param parent The ViewGroup into which the new View will be added after it is bound to an adapter position.
         * @param viewType The view type of the new View.
         * @return A new ViewHolder that holds a View of the given view type.
         */
        @NonNull
        @Override
        public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_saved_song, parent, false);
            return new SongViewHolder(itemView);
        }


        /**
         * Binds data to the ViewHolder.
         * @param holder The ViewHolder to bind data to.
         * @param position The position of the item within the adapter's data set.
         */
        @Override
        public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
            Song song = songs.get(position);
            holder.titleTextView.setText(song.getTitle());
            String imageUrl = "https://e-cdns-images.dzcdn.net/images/cover/"+song.getPicture()+"/1000x1000-000000-80-0-0.jpg";
            Picasso.get().load(imageUrl).into(holder.artistImageView);

            //holder.artistTextView.setText(song.getArtist());
            //holder.albumTextView.setText(song.getAlbumName());
            String duration = song.getSongDuration();
            // Add more bindings as needed
        }
        /**
         * Gets the total number of items in the data set held by the adapter.
         * @return The total number of items in the data set.
         */
        @Override
        public int getItemCount() {
            return songs == null ? 0 : songs.size();
        }



        /**
         * ViewHolder class for displaying individual songs in the RecyclerView.
         */
        public class SongViewHolder extends RecyclerView.ViewHolder {
            TextView titleTextView;

            ImageView artistImageView;


            /**
             * Constructor for the ViewHolder.
             * @param itemView The View object contained within this ViewHolder.
             */
            public SongViewHolder(@NonNull View itemView) {
                super(itemView);
                //titleTextView = itemView.findViewById(R.id.titleTextView);
                titleTextView = itemView.findViewById(R.id.artistTextView);
                //albumTextView = itemView.findViewById(R.id.albumTextView);
                //durationTextView = itemView.findViewById(R.id.durationTextView);
                artistImageView = itemView.findViewById(R.id.artistImageView);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            Song song = songs.get(position);
                            // Launch SongResultActivity with necessary data
                            Intent intent = new Intent(v.getContext(), SongResultActivity.class);
                            intent.putExtra("title", song.getTitle());
                            intent.putExtra("albumName", song.getAlbumName());
                            intent.putExtra("songDuration", song.getDuration());
                            intent.putExtra("albumCover", song.getAlbumCover());
                            intent.putExtra("artistName", song.getArtist());
                            intent.putExtra("SongPic", song.getPicture());
                            v.getContext().startActivity(intent);
                        }
                    }
                });
                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            // Get the song at the clicked position
                            Song song = songs.get(position);

                            // Show an AlertDialog to confirm song deletion
                            AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                            builder.setMessage(getString(R.string.songDelete2))
                                    .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Delete the song from the database
                                            Executors.newSingleThreadExecutor().execute(new Runnable() {
                                                @Override
                                                public void run() {
                                                    songDAO.delete(song);

                                                    // Refresh the RecyclerView to reflect the changes
                                                    List<Song> updatedSongs = songDAO.getAllSongDistinct();
                                                    ((SavedSongActivity) itemView.getContext()).runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            songAdapter.setSongs(updatedSongs);
                                                            // Show a Snackbar to indicate song deletion
                                                            Snackbar.make(itemView, getString(R.string.songDeletemsg), Snackbar.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                }
                                            });
                                        }
                                    })
                                    .setNegativeButton(getString(R.string.no), null)
                                    .show();
                        }
                        return true;
                    }
                });
            }
        }
    }
}