package org.algonquin.cst2355.finalproject.songsearch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.algonquin.cst2355.finalproject.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SongResultActivity extends AppCompatActivity {
    private List<Song> songList;
    private RecyclerView recyclerView;
    private SongSearchActivity.SongAdapter songAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_result);

    //    // Initialize RecyclerView and its adapter
    //    recyclerView = findViewById(R.id.songList_recycler_view);
    //    songList = new ArrayList<>();
    //    songAdapter = new SongAdapter(songList, new SongAdapter.OnItemClickListener() {
    //        @Override
    //        public void onItemClick(Song song) {
    //            // Handle item click
    //            if (song != null && song.getTrackList() != null) {
    //                // Start SongSearchActivity with the clicked song's tracklist URL
    //                Intent intent = new Intent(SongResultActivity.this, SongSearchActivity.class);
    //                intent.putExtra("tracklistUrl", song.getTrackList());
    //                startActivity(intent);
    //            }
    //        }
    //    });
    //    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    //    recyclerView.setAdapter(adapter);
    //
    //    // Retrieve the list of songs passed from the previous activity
    //    List<Song> receivedSongList = getIntent().getParcelableArrayListExtra("songList");
    //
    //    // Add the received songs to the adapter and notify data set changed
    //    if (receivedSongList != null) {
    //        songList.addAll(receivedSongList);
    //        adapter.notifyDataSetChanged();
    //    }
    }
}