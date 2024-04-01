package org.algonquin.cst2355.finalproject.songsearch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
/**
 * This activity allows users to search for songs by an artist using the Deezer API.
 * It displays the search results in a RecyclerView and allows users to click on a song
 * to view its details.
 * Author: Hoang Anh Nguyen - 041099695
 */
public class SongSearchActivity extends AppCompatActivity  {

    private EditText artistNameEditText;
    private Button searchButton;
    private RecyclerView recyclerView;
    private List<Song> songList;

    private SongAdapter songAdapter;
    private RequestQueue requestQueue;

    private SharedPreferences sharedPreferences;

    private static final String DEEZER_API_URL = "https://api.deezer.com/search/artist/?q=";
    private static final String PREFS_NAME = "SongSearchPrefs";
    private static final String ARTIST_NAME_KEY = "artistName";


    /**
     * Overrides the onCreateOptionsMenu method to inflate the menu resource file.
     * @param menu The menu to be displayed in the Toolbar.
     * @return true if the menu is inflated successfully, false otherwise.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_song_search, menu);
        return true;
    }
    /**
     * Overrides the onOptionsItemSelected method to handle menu item clicks.
     * @param item The menu item that was clicked.
     * @return true if the menu item click is handled, false otherwise.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_songList) {
            Intent intent = new Intent(this, SavedSongActivity.class);
            startActivity(intent);
            return true;
        } else if(id == R.id.AboutSongSearch){
            showHelp();
            return true;
        }else {
            return super.onOptionsItemSelected(item);
        }

    }

    /**
     * Displays a help dialog explaining the functionality of the activity.
     */
    private void showHelp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.songhelp));
        builder.setMessage(getString(R.string.songhelpdetail));
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
     * Overrides the onCreate method to initialize the activity.
     * @param savedInstanceState The saved instance state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_search);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

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

        artistNameEditText.setText(sharedPreferences.getString(ARTIST_NAME_KEY, ""));
        // Set click listener for the search button
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //searchArtist(artistNameEditText.getText().toString());
                String artistName = artistNameEditText.getText().toString();
                saveArtistName(artistName);

                searchArtist(artistName);

            }
        });
    }
    /**
     * Saves the artist name entered by the user in SharedPreferences.
     * @param artistName The name of the artist entered by the user.
     */
    private void saveArtistName(String artistName) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ARTIST_NAME_KEY, artistName);
        editor.apply();
    }


    /**
     * Searches for the artist using the Deezer API.
     * @param artistName The name of the artist to search for.
     */
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

                            // Call searchSong after retrieving tracklist URL
                            searchSong();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(SongSearchActivity.this, getString(R.string.Textmsg), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(SongSearchActivity.this, R.string.error_fetching_data_from_api, Toast.LENGTH_SHORT).show();
                    }
                });

        // Add the request to the RequestQueue
        requestQueue.add(jsonObjectRequest);
    }

    /**
     * Searches for songs by the artist using the Deezer API.
     */
    private void searchSong() {
        if (!songList.isEmpty()) {
            String albumTracklistUrl = songList.get(0).getTrackList();

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, albumTracklistUrl, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("Album JSON Response", response.toString());
                            try {
                                songList.clear();
                                // Extract song details from the album's tracklist
                                JSONArray data = response.getJSONArray("data");
                                List<String> songTracklistUrls = new ArrayList<>();
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject trackObject = data.getJSONObject(i);
                                    String songTitle = trackObject.getString("title");
                                    String md5Hash = trackObject.getString("md5_image");
                                    String songDuration = trackObject.getString("duration");

                                    JSONObject albumObject = trackObject.getJSONObject("album");
                                    String AlbumName = albumObject.getString("title");
                                    String AlbumCover=albumObject.getString("cover_xl");

                                    JSONObject artistObject = trackObject.getJSONObject("artist");
                                    String artistName = artistObject.getString("name");

                                    // Create a new Song object and add it to the songList
                                    Song song = new Song();
                                    song.setTitle(songTitle);
                                    song.setPicture(md5Hash);
                                    song.setAlbumName(AlbumName);
                                    song.setSongDuration(songDuration);
                                    song.setAlbumCover(AlbumCover);
                                    song.setArtist(artistName);
                                    songList.add(song); // Add the song to the list
                                }

                                // Notify the adapter of changes
                                songAdapter.notifyDataSetChanged();

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(SongSearchActivity.this, R.string.error_parsing_json_response, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            Toast.makeText(SongSearchActivity.this, R.string.error_fetching_album_data_from_api, Toast.LENGTH_SHORT).show();
                        }
                    });

            // Add the request to the RequestQueue
            requestQueue.add(jsonObjectRequest);
        }
    }


    /**
     * This RecyclerView adapter class binds song data to the RecyclerView.
     */
    public static class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {

        private List<Song> songList;


        public interface OnItemClickListener {
            void onItemClick(Song song);
        }

        /**
         * Constructor for the SongAdapter class.
         * @param songList The list of songs to be displayed.
         */
        public SongAdapter(List<Song> songList) {

            this.songList = songList;

        }
        /**
         * Creates ViewHolders for the RecyclerView items.
         * @param parent The parent ViewGroup.
         * @param viewType The type of view.
         * @return A new SongViewHolder instance.
         */
        @NonNull
        @Override
        public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_artist, parent, false);
            return new SongViewHolder(view);
        }
        /**
         * Binds song data to the RecyclerView items.
         * @param holder The ViewHolder.
         * @param position The position of the item in the list.
         */
        @Override
        public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
            Song song = songList.get(position);
            holder.bind(song);
        }
        /**
         * Gets the total number of items in the list.
         * @return The total number of items in the list.
         */
        @Override
        public int getItemCount() {
            return songList.size();
        }
        /**
         * This ViewHolder class represents individual items in the RecyclerView.
         */
        static class SongViewHolder extends RecyclerView.ViewHolder {

            TextView titleTextView;

            ImageView artistImageView;
            /**
             * Constructor for the SongViewHolder class.
             * @param itemView The item view.
             */
            public SongViewHolder(@NonNull View itemView) {
                super(itemView);
                titleTextView = itemView.findViewById(R.id.artistTextView);
                //id=itemView.findViewById(R.id.artistIDTextView);
                artistImageView = itemView.findViewById(R.id.artistImageView);

            }
            /**
             * Binds song data to the ViewHolder.
             * @param song The song object.
             */
            public void bind(Song song) {
                titleTextView.setText(song.getTitle());
                //id.setText(String.valueOf(song.getAlbumName())); // Set text instead of setting ID
                String imageUrl = "https://e-cdns-images.dzcdn.net/images/cover/"+song.getPicture()+"/1000x1000-000000-80-0-0.jpg";
                Picasso.get().load(imageUrl).into(artistImageView);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Launch SongResultActivity with necessary data
                        Intent intent = new Intent(v.getContext(), SongResultActivity.class);
                        //intent.putExtra("song", song); // Pass the selected song object
                        intent.putExtra("title", song.getTitle());
                        intent.putExtra("albumName", song.getAlbumName());
                        intent.putExtra("songDuration", song.getSongDuration());
                        intent.putExtra("albumCover", song.getAlbumCover());
                        intent.putExtra("artistName", song.getArtist());
                        intent.putExtra("SongPic", song.getPicture());
                        v.getContext().startActivity(intent);
                    }
                });
            }
        }
    }
}