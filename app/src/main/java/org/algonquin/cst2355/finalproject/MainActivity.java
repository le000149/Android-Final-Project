package org.algonquin.cst2355.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import org.algonquin.cst2355.finalproject.dictionary.DictionaryActivity;
import org.algonquin.cst2355.finalproject.recipe.RecipeActivity;
import org.algonquin.cst2355.finalproject.songsearch.SongSearchActivity;
import org.algonquin.cst2355.finalproject.sunrisesunset.SunriseSunsetActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Intent intent = null;
        if (id == R.id.sunrise_sunset) {
            intent = new Intent(MainActivity.this, SunriseSunsetActivity.class);
        } else if (id == R.id.recipe) {
            intent = new Intent(MainActivity.this, RecipeActivity.class);
        } else if (id == R.id.dictionary) {
            intent = new Intent(MainActivity.this, DictionaryActivity.class);
        } else if (id == R.id.song_search) {
            intent = new Intent(MainActivity.this, SongSearchActivity.class);
        }
        if (intent != null) {
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}