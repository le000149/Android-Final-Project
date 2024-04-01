package org.algonquin.cst2355.finalproject.dictionary;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import org.algonquin.cst2355.finalproject.MainApplication;
import org.algonquin.cst2355.finalproject.R;
import org.algonquin.cst2355.finalproject.databinding.ActivityDictionaryBinding;
import org.algonquin.cst2355.finalproject.dictionary.model.Definition;

import java.util.List;
import java.util.concurrent.Executors;

public class DictionaryActivity extends AppCompatActivity {
    public static final String SP_KEY_LAST_SEARCH_WORD = "last_search_word";
    public static final String SP_NAME = "dict";
    private static final String TAG = "DictionaryActivity";

    private ActivityDictionaryBinding binding;
    private DefinitionAdapter definitionAdapter;
    private List<Definition> definitions;
    private List<Definition> deletedDefinitions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDictionaryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //show the last searched word
        SharedPreferences sharedPreferences = getSharedPreferences(SP_NAME, MODE_PRIVATE);
        binding.searchEditText.setText(sharedPreferences.getString(SP_KEY_LAST_SEARCH_WORD, ""));
        binding.searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            Log.d(TAG, "onCreate: " + actionId + " " + event);
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                binding.searchButton.performClick();
            }
            return true;
        });

        //add click listener to search button
        binding.searchButton.setOnClickListener(v -> {
            String word = binding.searchEditText.getText().toString();
            if (word.isEmpty()) {
                Toast.makeText(this, R.string.enter_word_to_search, Toast.LENGTH_SHORT).show();
            } else {
                //save the word to shared preferences
                sharedPreferences.edit().putString(SP_KEY_LAST_SEARCH_WORD, word).apply();
                //search for the word
                DictionaryResultActivity.launch(this, word, true);
            }
        });
        showSavedDefinitionList();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();
        showSavedDefinitionList();
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
                    .setTitle(R.string.help)
                    .setMessage(R.string.dict_help)
                    .setPositiveButton(R.string.ok, (dialog, which) -> dialog.dismiss())
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        getOnBackPressedDispatcher().onBackPressed();
        return true;
    }

    private void showSavedDefinitionList() {
        Executors.newSingleThreadExecutor().execute(() -> {
            definitions = MainApplication.getDictionaryDB().DefinitionDao().getAllDefinitionDistinct();
            runOnUiThread(() -> {
                definitionAdapter = new DefinitionAdapter();
                binding.savedDefinitionRecyclerView.setAdapter(definitionAdapter);
                binding.savedDefinitionRecyclerView.setLayoutManager(new LinearLayoutManager(DictionaryActivity.this));
            });
        });
    }

    private void askDelDefinition(Definition definition, int position) {
        new AlertDialog.Builder(DictionaryActivity.this)
                .setTitle(getString(R.string.delete_definition, definition.getWord()))
                .setPositiveButton(R.string.yes, (dialog, which) -> delDefinition(definition, position))
                .setNegativeButton(R.string.no, (dialog, which) -> {
                })
                .show();
    }

    private void delDefinition(Definition definition, int position) {
        definitions.remove(position);
        definitionAdapter.notifyItemRemoved(position);
        Snackbar.make(binding.savedDefinitionRecyclerView, R.string.definition_deleted, Snackbar.LENGTH_SHORT).setAction(R.string.undo, v -> {
            if (deletedDefinitions != null && deletedDefinitions.get(0) != null) {
                definitions.add(position, deletedDefinitions.get(0));
                definitionAdapter.notifyItemInserted(position);
                Executors.newSingleThreadExecutor().execute(() -> {
                    if (deletedDefinitions != null) {
                        MainApplication.getDictionaryDB().DefinitionDao().saveDefinitions(deletedDefinitions);
                    }
                });
            }
        }).show();
        Executors.newSingleThreadExecutor().execute(() -> {
            deletedDefinitions = MainApplication.getDictionaryDB().DefinitionDao().getDefinitions(definition.getWord());
            MainApplication.getDictionaryDB().DefinitionDao().deleteDefinition(definition.getWord());
        });
    }

    public class DefinitionAdapter extends RecyclerView.Adapter<DefinitionViewHolder> {

        public DefinitionAdapter() {
        }

        @NonNull
        @Override
        public DefinitionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dict_word, parent, false);
            return new DefinitionViewHolder(view);
        }

        @Override
        public void onBindViewHolder(DefinitionViewHolder holder, int position) {
            Definition definition = definitions.get(position);
            holder.bind(definition, position);
        }

        @Override
        public int getItemCount() {
            return definitions.size();
        }

    }

    public class DefinitionViewHolder extends RecyclerView.ViewHolder {
        TextView wordTextView;
        TextView definitionTextView;

        public DefinitionViewHolder(View itemView) {
            super(itemView);
            wordTextView = itemView.findViewById(R.id.word_text_view);
            definitionTextView = itemView.findViewById(R.id.definition_text_view);
        }

        public void bind(Definition definition, int position) {
            wordTextView.setText(definition.getWord());
            definitionTextView.setText(definition.getDefinition());
            itemView.setOnClickListener(v -> DictionaryResultActivity.launch(v.getContext(), definition.getWord(), false));
            itemView.setOnLongClickListener(v -> {
                askDelDefinition(definition, position);
                return false;
            });
        }

    }

}