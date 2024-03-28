package org.algonquin.cst2355.finalproject.songsearch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SongSearchActivity extends AppCompatActivity  {

    private EditText artistNameEditText;
    private Button searchButton;
    private RecyclerView recyclerView;
    private List<Song> songList;
    private List<Song> songList2;
    private SongAdapter songAdapter;
    private RequestQueue requestQueue;
    private SongDAO songDAO;

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
        // Initialize Room database
        //songDatabase = Room.databaseBuilder(getApplicationContext(), SongSearchDatabase.class, SongSearchDatabase.NAME)
        //        .build();
        //songDAO = songDatabase.SongDao(); // Get DAO instance


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

                            // Parse response and extract artist details
                            JSONArray data = response.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject artistObject = data.getJSONObject(i);
                                String tracklistUrl = artistObject.getString("tracklist");


                                // Create a Song object and add tracklist URL
                                Song song = new Song();
                                song.setTrackList(tracklistUrl);
                                songList.add(song);
                            }

                            // Notify adapter of changes
                            songAdapter.notifyDataSetChanged();

                            // Call searchAlbum after retrieving tracklist URL
                            searchAlbum();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(SongSearchActivity.this, "Error parsing JSON response", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(SongSearchActivity.this, "Error fetching data from API1", Toast.LENGTH_SHORT).show();
                    }
                });

        // Add the request to the RequestQueue
        requestQueue.add(jsonObjectRequest);
    }


    private void searchAlbum() {
        if (!songList.isEmpty()) {
            String albumTracklistUrl = songList.get(0).getTrackList();

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, albumTracklistUrl, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("Album JSON Response", response.toString());
                            try {
                                // Extract song details from the album's tracklist
                                JSONArray data = response.getJSONArray("data");
                                List<String> songTracklistUrls = new ArrayList<>();
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject trackObject = data.getJSONObject(i);
                                    JSONObject albumObject = trackObject.getJSONObject("album");
                                    String songTracklistUrl = albumObject.getString("tracklist");

                                    songTracklistUrls.add(songTracklistUrl);



                                }


                                // Call searchSong with the list of song tracklist URLs
                                searchSong(songTracklistUrls);

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(SongSearchActivity.this, "Error parsing JSON response2", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            Toast.makeText(SongSearchActivity.this, "Error fetching album data from API", Toast.LENGTH_SHORT).show();
                        }
                    });

            // Add the request to the RequestQueue
            requestQueue.add(jsonObjectRequest);
        }
    }

    private void searchSong(List<String> songTracklistUrls) {
        // Get the tracklist URL from the first song in the list
        songList.clear();

        // Fetch songs from each song tracklist URL
        for (String songTracklistUrl : songTracklistUrls) {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, songTracklistUrl, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                // Parse response and extract song details
                                JSONArray data = response.getJSONArray("data");
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject songObject = data.getJSONObject(i);
                                    String songTitle = songObject.getString("title");
                                    String md5Hash = songObject.getString("md5_image");

                                    Log.d("Song Title", songTitle);
                                    Log.d("MD5 Hash", md5Hash);
                                            // Create a Song object and add song details
                                    Song song = new Song();
                                    song.setTitle(songTitle);
                                    song.setPicture(md5Hash);


                                    songList.add(song);
                                }

                                // Notify adapter of changes after adding all songs from this tracklist
                                songAdapter.notifyDataSetChanged();

                            } catch (JSONException e) {
                                e.printStackTrace();
                                //Toast.makeText(SongSearchActivity.this, "Error parsing JSON response3", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            Toast.makeText(SongSearchActivity.this, "Error fetching song data from API", Toast.LENGTH_SHORT).show();
                        }
                    });

            // Add the request to the RequestQueue
            requestQueue.add(jsonObjectRequest);
        }
    }

    public static class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {

        private List<Song> songList;


        public interface OnItemClickListener {
            void onItemClick(Song song);
        }
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
                //id=itemView.findViewById(R.id.artistIDTextView);
                artistImageView = itemView.findViewById(R.id.artistImageView);

            }

            public void bind(Song song) {
                titleTextView.setText(song.getTitle());
                //id.setText(String.valueOf(song.getId())); // Set text instead of setting ID
                String imageUrl = "https://e-cdns-images.dzcdn.net/images/cover/"+song.getPicture()+"/1000x1000-000000-80-0-0.jpg";
                Picasso.get().load(imageUrl).into(artistImageView);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Launch SongResultActivity with necessary data
                        Intent intent = new Intent(v.getContext(), SongResultActivity.class);
                        //intent.putExtra("song", song); // Pass the selected song object
                        v.getContext().startActivity(intent);
                    }
                });
            }
        }
    }
}