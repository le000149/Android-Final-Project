package algonquin.cst2335.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonApp1 = findViewById(R.id.buttonApp1);
        Button buttonApp2 = findViewById(R.id.buttonApp2);
        Button buttonApp3 = findViewById(R.id.buttonApp3);
        Button buttonApp4 = findViewById(R.id.buttonApp4);

        // Launch SunriseSunsetActivity
        buttonApp1.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, algonquin.cst2335.finalproject.sunrise.SunriseSunsetActivity.class);
            startActivity(intent);
        });

        // Launch RecipeActivity
        buttonApp2.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, algonquin.cst2335.finalproject.recipe.RecipeActivity.class);
            startActivity(intent);
        });

        // Launch SongActivity
        buttonApp3.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, algonquin.cst2335.finalproject.song.SongActivity.class);
            startActivity(intent);
        });

        // Launch DictionaryActivity
        buttonApp4.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, algonquin.cst2335.finalproject.dictionary.DictionaryActivity.class);
            startActivity(intent);
        });
    }
}
