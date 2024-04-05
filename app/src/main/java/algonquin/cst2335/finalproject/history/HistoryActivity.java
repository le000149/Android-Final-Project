package algonquin.cst2335.finalproject.history;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


import algonquin.cst2335.finalproject.R;
import algonquin.cst2335.finalproject.ui.DictionaryEntry;
import algonquin.cst2335.finalproject.ui.MessageDatabase;

import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize RecyclerView
        RecyclerView recyclerView = findViewById(R.id.historyDisplay);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get the word and definition passed from DictionaryActivity
        Intent intent = getIntent();
        String word = intent.getStringExtra("word");
        String definition = intent.getStringExtra("definition");

        // Create a list with the word and definition
        List<DictionaryEntry> historyEntries = new ArrayList<>();
        historyEntries.add(new DictionaryEntry(word, Collections.singletonList(definition)));

        // Create and set the adapter
        HistoryAdapter historyAdapter = new HistoryAdapter(historyEntries);
        recyclerView.setAdapter(historyAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.historymenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.viewHistory) {
            // Delete the word and definition from the database
            deleteWordAndDefinitionFromDatabase();
            return true; // Return true to indicate that the event has been handled
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void deleteWordAndDefinitionFromDatabase() {
        // Get the word and definition passed from DictionaryActivity
        Intent intent = getIntent();
        String word = intent.getStringExtra("word");
        String definition = intent.getStringExtra("definition");

        // Get an instance of the MessageDatabase
        MessageDatabase messageDatabase = Room.databaseBuilder(getApplicationContext(), MessageDatabase.class, "database-name").build();

        // Execute database operation asynchronously
        Executor thread = Executors.newSingleThreadExecutor();
        thread.execute(() -> {
            // Delete the word and definition from the database
            messageDatabase.dmDAO().deleteWordAndDefinition(word, definition);

            runOnUiThread(() -> {
                // Show a toast indicating successful deletion
                Toast.makeText(HistoryActivity.this, "Word and definition deleted from history", Toast.LENGTH_SHORT).show();

                // Finish the activity to return to the previous screen
                finish();
            });
        });
    }
}



