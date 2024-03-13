package org.algonquin.cst2355.finalproject.dictionary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.algonquin.cst2355.finalproject.MainApplication;
import org.algonquin.cst2355.finalproject.R;
import org.algonquin.cst2355.finalproject.databinding.ActivityDictionaryBinding;

import java.util.List;
import java.util.concurrent.Executors;

public class DictionaryActivity extends AppCompatActivity {

    public static final String SP_KEY_LAST_SEARCH_WORD = "last_search_word";
    public static final String SP_NAME = "dict";

    private ActivityDictionaryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDictionaryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        //show the last searched word
        SharedPreferences sharedPreferences = getSharedPreferences(SP_NAME, MODE_PRIVATE);
        binding.searchEditText.setText(sharedPreferences.getString(SP_KEY_LAST_SEARCH_WORD, ""));

        //add click listener to search button
        binding.searchButton.setOnClickListener(v -> {
            String word = binding.searchEditText.getText().toString();
            if (word.isEmpty()) {
                Toast.makeText(this, "Please enter a word to search", Toast.LENGTH_SHORT).show();
            } else {
                //save the word to shared preferences
                sharedPreferences.edit().putString(SP_KEY_LAST_SEARCH_WORD, word).apply();
                //search for the word
                DictionaryResultActivity.launch(this, word);
            }
        });

        showSavedWords();
    }

    private void showSavedWords() {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                List<String> words = MainApplication.getDictionaryDB().DefinitionDao().getAllDefinitionDistinct();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        binding.savedWordRecyclerView.setAdapter(new DefinitionAdapter(words));
                        binding.savedWordRecyclerView.setLayoutManager(new LinearLayoutManager(DictionaryActivity.this));
                    }
                });
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
                    .setPositiveButton(R.string.ok, (dialog, which) -> dialog.dismiss())
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }

    public static class DefinitionAdapter extends RecyclerView.Adapter<DefinitionAdapter.DefinitionViewHolder> {

        private final List<String> definitions;

        public DefinitionAdapter(List<String> definitions) {
            this.definitions = definitions;
        }

        @NonNull
        @Override
        public DefinitionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dict_word, parent, false);
            return new DefinitionViewHolder(view);
        }

        @Override
        public void onBindViewHolder(DefinitionViewHolder holder, int position) {
            String definition = definitions.get(position);
            holder.bind(definition);
        }

        @Override
        public int getItemCount() {
            return definitions.size();
        }

        static class DefinitionViewHolder extends RecyclerView.ViewHolder {

            TextView definitionTextView;

            public DefinitionViewHolder(View itemView) {
                super(itemView);
                definitionTextView = itemView.findViewById(R.id.wordTextView);
            }

            public void bind(String definition) {
                definitionTextView.setText(definition);
            }
        }
    }


}