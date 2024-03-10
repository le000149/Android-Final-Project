package org.algonquin.cst2355.finalproject.dictionary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.algonquin.cst2355.finalproject.R;
import org.algonquin.cst2355.finalproject.databinding.ActivityDictionaryBinding;

public class DictionaryActivity extends AppCompatActivity {

    private ActivityDictionaryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDictionaryBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.searchButton.setOnClickListener(v -> {
            String word = binding.searchEditText.getText().toString();
            if (word.isEmpty()) {
                Toast.makeText(this, "Please enter a word to search", Toast.LENGTH_SHORT).show();
            } else {
                //search for the word
                DictionaryResultActivity.launch(this, word);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_dictionary, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.help) {
            //show help alert dialog
            new AlertDialog.Builder(this)
                    .setTitle("Help")
                    .setMessage("This is a dictionary app. You can search for a word and get its definition.")
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }

}