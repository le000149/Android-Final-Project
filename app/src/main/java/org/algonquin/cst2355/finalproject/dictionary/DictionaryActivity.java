package org.algonquin.cst2355.finalproject.dictionary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
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
    private List<Definition> definitions;
    private ActivityDictionaryBinding binding;
    private List<Definition> deletedDefinitions;
    private DefinitionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDictionaryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                Toast.makeText(this, "Please enter a word to search", Toast.LENGTH_SHORT).show();
            } else {
                //save the word to shared preferences
                sharedPreferences.edit().putString(SP_KEY_LAST_SEARCH_WORD, word).apply();
                //search for the word
                DictionaryResultActivity.launch(this, word, true);
            }
        });

        showSavedWords();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();
        showSavedWords();
    }

    private void showSavedWords() {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                definitions = MainApplication.getDictionaryDB().DefinitionDao().getAllDefinitionDistinct();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new DefinitionAdapter();
                        binding.savedWordRecyclerView.setAdapter(adapter);
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
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        getOnBackPressedDispatcher().onBackPressed();
        return true;
    }

    private void askDelDefinition(Definition definition, int position) {
        new AlertDialog.Builder(DictionaryActivity.this)
                .setTitle("Delete " + definition.getWord() + " ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delDefinition(definition, position);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    private void delDefinition(Definition definition, int position) {
        definitions.remove(position);
        adapter.notifyItemRemoved(position);
        Snackbar.make(binding.savedWordRecyclerView, "Item deleted", Snackbar.LENGTH_SHORT).setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deletedDefinitions != null && deletedDefinitions.get(0) != null) {
                    definitions.add(position, deletedDefinitions.get(0));
                    adapter.notifyItemInserted(position);
                    Executors.newSingleThreadExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            if (deletedDefinitions != null) {
                                MainApplication.getDictionaryDB().DefinitionDao().saveDefinitions(deletedDefinitions);
                            }
                        }
                    });
                }
            }
        }).show();
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                deletedDefinitions = MainApplication.getDictionaryDB().DefinitionDao().getDefinitions(definition.getWord());
                MainApplication.getDictionaryDB().DefinitionDao().deleteDefinition(definition.getWord());
            }
        });
    }

    public class DefinitionAdapter extends RecyclerView.Adapter<DefinitionAdapter.DefinitionViewHolder> {

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

        class DefinitionViewHolder extends RecyclerView.ViewHolder {

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
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DictionaryResultActivity.launch(v.getContext(), definition.getWord(), false);
                    }
                });
                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        askDelDefinition(definition, position);
                        return false;
                    }
                });
            }

        }
    }


}