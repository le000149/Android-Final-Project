package org.algonquin.cst2355.finalproject.songsearch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;

import org.algonquin.cst2355.finalproject.R;
import org.algonquin.cst2355.finalproject.databinding.ActivityDictionaryBinding;
import org.algonquin.cst2355.finalproject.databinding.ActivitySongSearchBinding;

public class SongSearchActivity extends AppCompatActivity {

    private SongDAO Song;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_search);

        SongSearchDatabase db = Room.databaseBuilder(getApplicationContext(), SongSearchDatabase.class, "SongSearchDatabase").build();
        Song = db.SongDao();
    }
}