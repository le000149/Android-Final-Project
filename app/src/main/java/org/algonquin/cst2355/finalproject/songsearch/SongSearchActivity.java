package org.algonquin.cst2355.finalproject.songsearch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.algonquin.cst2355.finalproject.R;
import org.algonquin.cst2355.finalproject.databinding.ActivitySongSearchBinding;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SongSearchActivity extends AppCompatActivity {

    private EditText artistNameEditText;
    private Button searchButton;
    private RecyclerView recyclerView;
    private List<Song> songList;
    private SongAdapter songAdapter;
    private RequestQueue requestQueue;

    private static final String DEEZER_API_URL = "https://api.deezer.com/search/artist/?q=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_search);

        // Bind views
        artistNameEditText = findViewById(R.id.artistName);
        searchButton = findViewById(R.id.buttonSearch);
        recyclerView = findViewById(R.id.saved_song_recycler_view);

        // Initialize RecyclerView and its adapter
        songList = new ArrayList<>();
        songAdapter = new SongAdapter(songList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(songAdapter);

        // Initialize Volley RequestQueue
        requestQueue = Volley.newRequestQueue(this);

        // Set click listener for the search button
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchArtist(artistNameEditText.getText().toString());
            }
        });
    }

    private void searchArtist(String artistName) {
        String url = DEEZER_API_URL + artistName;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Clear previous search results
                            songList.clear();
                            //songList =new ArrayList<>();

                            // Parse response and extract song details
                            JSONArray data = response.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject artistObject = data.getJSONObject(i);
                                String artistName = artistObject.getString("name");

                                String artistId = artistObject.getString("id");
                                String tracklistUrl = artistObject.getString("tracklist");
                                String artistPic = artistObject.getString("picture_medium");
                                // You may fetch additional details of the artist if needed
                                // For example, you can fetch the artist's top tracks using another API call

                                // Create a Song object and add it to the list
                                Song song = new Song();
                                song.setTitle(artistName);
                                song.setId(Integer.parseInt(artistId));
                                song.setPicture(artistPic);
                                //song.setTracklistUrl(tracklistUrl);
                                songList.add(song);
                            }

                            // Notify adapter of changes
                            songAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(SongSearchActivity.this, "Error parsing JSON response", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(SongSearchActivity.this, "Error fetching data from API", Toast.LENGTH_SHORT).show();
                    }
                });

        // Add the request to the RequestQueue.
        requestQueue.add(jsonObjectRequest);
    }

    public static class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {

        private List<Song> songList;

        public SongAdapter(List<Song> songList) {
            this.songList = songList;
        }

        @NonNull
        @Override
        public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_artist, parent, false);
            return new SongViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
            Song song = songList.get(position);
            holder.bind(song);
        }

        @Override
        public int getItemCount() {
            return songList.size();
        }

        static class SongViewHolder extends RecyclerView.ViewHolder {

            TextView titleTextView;
            TextView id;
            ImageView artistImageView;
            public SongViewHolder(@NonNull View itemView) {
                super(itemView);
                titleTextView = itemView.findViewById(R.id.artistTextView);
                id=itemView.findViewById(R.id.artistIDTextView);
                artistImageView = itemView.findViewById(R.id.artistImageView);
            }

            public void bind(Song song) {
                titleTextView.setText(song.getTitle());
                id.setText(String.valueOf(song.getId())); // Set text instead of setting ID
                String imageUrl = song.getPicture();
                Picasso.get().load(imageUrl).into(artistImageView);
            }
        }
    }
}