package org.algonquin.cst2355.finalproject.songsearch;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.concurrent.Executors;


//import com.android.volley.Request;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;

import org.algonquin.cst2355.finalproject.R;


public class SongResultActivity extends AppCompatActivity {
    //private List<Song> songList;

    private TextView titleTextView;
    private TextView albumNameTextView;
    private TextView durationTextView;
    private ImageView albumCover;
    private TextView Artist;
    private SongDAO songDAO;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_song_result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            // Handle save action here
            saveMusicItem();
            return true;
        }else if(item.getItemId() == R.id.AboutSongSavePage){
            showHelp();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void showHelp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.SongSavePD));
        builder.setMessage(getString(R.string.SongSavePage));
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

    private void saveMusicItem() {
        Intent intent = getIntent();
        if (intent != null) {
            String title = intent.getStringExtra("title");
            String albumName = intent.getStringExtra("albumName");
            String duration = intent.getStringExtra("songDuration");
            String albumCover = intent.getStringExtra("albumCover");
            String artistName = intent.getStringExtra("artistName");
            String SongPic = intent.getStringExtra("SongPic");


            if (songDAO == null) {
                Toast.makeText(this, getString(R.string.SongSavemsg4), Toast.LENGTH_SHORT).show();
                return;
            }

            Executors.newSingleThreadExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        //songDAO.deleteAll();
                        // Check if the song already exists in the database
                        Song existingSong = songDAO.getSongByTitle(title);
                        if (existingSong != null) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(SongResultActivity.this, getString(R.string.SongSavemsg2), Toast.LENGTH_SHORT).show();
                                }
                            });
                            return; // Exit the method without saving the song again
                        }

                        // Create a new Song object and save it
                        Song song = new Song();
                        song.setTitle(title);
                        song.setAlbumName(albumName);
                        song.setDuration(duration);
                        song.setAlbumCover(albumCover);
                        song.setArtist(artistName);
                        song.setPicture(SongPic);
                        song.setDuration(duration);

                        songDAO.saveSong(song);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(SongResultActivity.this, getString(R.string.SongSavemsg), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(SongResultActivity.this, getString(R.string.SongSavemsg3) + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_result);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SongSearchDatabase songDatabase = Room.databaseBuilder(getApplicationContext(),
                SongSearchDatabase.class, "SongSearchDatabase").build();
        songDAO = songDatabase.SongDao(); // Initialize the DAO

        titleTextView = findViewById(R.id.SongName);
        albumNameTextView = findViewById(R.id.AlbumName);
        durationTextView = findViewById(R.id.Duration);
        albumCover=findViewById(R.id.AlbumCoverView);
        Artist=findViewById(R.id.ArtistName);
        Intent intent = getIntent();
        if (intent != null) {
            String title = intent.getStringExtra("title");
            String albumName = intent.getStringExtra("albumName");
            String duration = intent.getStringExtra("songDuration");
            String AlbumCover = intent.getStringExtra("albumCover");
            String artistName = intent.getStringExtra("artistName");

            // Display data in views
            titleTextView.setText(title);
            albumNameTextView.setText(albumName);
            durationTextView.setText(getString(R.string.SongDetailDura)+"  "+duration+" " +getString(R.string.SongDetailSec));
            Picasso.get().load(AlbumCover).into(albumCover);
            Artist.setText(getString(R.string.SongDetailArt)+"  " +artistName);
        }

    }




}