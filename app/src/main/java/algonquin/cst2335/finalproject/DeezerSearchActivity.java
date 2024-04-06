package algonquin.cst2335.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import algonquin.cst2335.finalproject.databinding.ActivityDeezerSearchBinding;

public class DeezerSearchActivity extends AppCompatActivity {

    private ActivityDeezerSearchBinding binding;
    private SongAdapter adapter;

    private List<Song> songs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDeezerSearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("Song Search");

        binding.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchArtist();
            }
        });

        binding.searchButton.setOnClickListener(v -> searchArtist());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SongAdapter(songs, (SongAdapter.OnSongInteractionListener) this);
        binding.recyclerView.setAdapter(adapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_help) {
            // Show a help message
            Toast.makeText(this, "Help message", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_about) {
            // Show an about message
            Toast.makeText(this, "About message", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void searchArtist() {
        String artistName = binding.editTextSearch.getText().toString().trim();
        if (!artistName.isEmpty()) {
            String url = "https://api.deezer.com/search/artist/?q=" + artistName;
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray data = response.getJSONArray("data");
                                if (data.length() > 0) {
                                    JSONObject artist = data.getJSONObject(0);
                                    String tracklistUrl = artist.getString("tracklist");
                                    fetchSongs(tracklistUrl);
                                } else {
                                    Toast.makeText(DeezerSearchActivity.this, "Artist not found", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(DeezerSearchActivity.this, "Error occurred", Toast.LENGTH_SHORT).show();
                        }
                    });
            Volley.newRequestQueue(this).add(jsonObjectRequest);
        } else {
            Toast.makeText(DeezerSearchActivity.this, "Please enter an artist name", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchSongs(String tracklistUrl) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, tracklistUrl, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray data = response.getJSONArray("data");
                            ArrayList<Song> songs = new ArrayList<>();
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject songJson = data.getJSONObject(i);
                                String title = songJson.getString("title");
                                String duration = songJson.getString("duration");
                                String albumName = songJson.getString("album_name");
                                String albumCoverUrl = songJson.getString("album_cover_url");
                                Song song = new Song(title, duration, albumName, albumCoverUrl);
                                songs.add(song);
                            }
                            adapter.setSongs(songs);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DeezerSearchActivity.this, "Error fetching songs", Toast.LENGTH_SHORT).show();
                    }
                });
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }


}
