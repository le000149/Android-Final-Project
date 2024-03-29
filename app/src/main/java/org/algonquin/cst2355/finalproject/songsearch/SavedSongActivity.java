package org.algonquin.cst2355.finalproject.songsearch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.algonquin.cst2355.finalproject.R;

import java.util.List;
import java.util.concurrent.Executors;

public class SavedSongActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SongAdapter songAdapter;
    private SongDAO songDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_song);

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

    public static class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {
        private List<Song> songs;

        public void setSongs(List<Song> songs) {
            this.songs = songs;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_saved_song, parent, false);
            return new SongViewHolder(itemView);
        }



        @Override
        public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
            Song song = songs.get(position);
            holder.titleTextView.setText(song.getTitle());
            String imageUrl = "https://e-cdns-images.dzcdn.net/images/cover/"+song.getPicture()+"/1000x1000-000000-80-0-0.jpg";
            Picasso.get().load(imageUrl).into(holder.artistImageView);

            //holder.artistTextView.setText(song.getArtist());
            //holder.albumTextView.setText(song.getAlbumName());
            //holder.durationTextView.setText(song.getDuration());
            // Add more bindings as needed
        }

        @Override
        public int getItemCount() {
            return songs == null ? 0 : songs.size();
        }



        public class SongViewHolder extends RecyclerView.ViewHolder {
            TextView titleTextView;
            //TextView artistTextView;
            //TextView albumTextView;
            //TextView durationTextView;

            ImageView artistImageView;



            public SongViewHolder(@NonNull View itemView) {
                super(itemView);
                //titleTextView = itemView.findViewById(R.id.titleTextView);
                titleTextView = itemView.findViewById(R.id.artistTextView);
                //albumTextView = itemView.findViewById(R.id.albumTextView);
                //durationTextView = itemView.findViewById(R.id.durationTextView);
                artistImageView = itemView.findViewById(R.id.artistImageView);
            }
        }
    }
}