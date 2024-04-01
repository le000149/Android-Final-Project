package org.algonquin.cst2355.finalproject.dictionary;

/**
 * This is for the CST2355 Final Project - Dictionary Topic
 * Author: Yeqing Xia
 * Lab Section: CST2335 013
 * Creation Date: 2024/03/31
 */

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

/**
 * The DictionaryActivity class is responsible for providing the interface to search for word definitions,
 * display saved definitions, and manage definition data.
 */
public class DictionaryActivity extends AppCompatActivity {

    /**
     * Key for storing the last search word in SharedPreferences.
     */
    public static final String SP_KEY_LAST_SEARCH_WORD = "last_search_word";

    /**
     * SharedPreferences name for the DictionaryActivity.
     */
    public static final String SP_NAME = "dict";

    /**
     * Tag used for logging in the DictionaryActivity
     */
    private static final String TAG = "DictionaryActivity";

    /**
     * Binding instance for accessing the layout‘s views.
     */
    private ActivityDictionaryBinding binding; //auto generate the binding via layout.

    /**
     * Adapter for managing the display of saved definitions in a RecyclerView.
     */
    private DefinitionAdapter savedDefinitionAdapter;

    /**
     * List of saved Definitions retrieved from the database.
     */
    private List<Definition> savedDefinitions;

    /**
     * Temporary list for storing definitions that have recently been deleted to allow undo operation.
     */
    private List<Definition> deletedDefinitions;//for deleted definitions undo.


    /**
     * This method initializes the activity, sets up the toolbar, search functionality
     * and kicks off the saved definitions display process.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDictionaryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            //show the back arrow on the toolbar.
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

        binding.toggleSavedDefinitions.setOnClickListener(v -> {
            if (binding.savedDefinitionRecyclerView.getVisibility() == View.VISIBLE) {
                binding.savedDefinitionRecyclerView.setVisibility(View.GONE);
                binding.toggleSavedDefinitions.setText(R.string.show_saved_definitions);
            } else {
                binding.savedDefinitionRecyclerView.setVisibility(View.VISIBLE);
                binding.toggleSavedDefinitions.setText(R.string.hide_saved_definitions);
            }
        });

        showSavedDefinitionList();
    }

    /**
     * This method is called when the activity is resumed.
     */
    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();
        showSavedDefinitionList();
    }

    /**
     * This method initializes the menu with the help option
     * @param menu The options menu in which the items are placed.
     * @return Return true for the menu to be displayed.
     */
    //create the help menu icon
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_dictionary, menu);
        return true;
    }

    /**
     * This method is called when the help menu item is selected.
     * @param item The menu item that was selected.
     * @return Return true to consume the event, false to let it propagate.
     */
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

    /**
     * This method is called when the back arrow is pressed.
     * @return true if the event was handled, false otherwise
     */
    @Override
    public boolean onSupportNavigateUp() {
        getOnBackPressedDispatcher().onBackPressed();
        return true;
    }

    /**
     * This method is used to show the saved definitions in a RecyclerView.
     */
    private void showSavedDefinitionList() {
        Executors.newSingleThreadExecutor().execute(() -> {
            savedDefinitions = MainApplication.getDictionaryDB().DefinitionDao().getAllDefinitionDistinct();
            runOnUiThread(() -> {
                savedDefinitionAdapter = new DefinitionAdapter();
                binding.savedDefinitionRecyclerView.setAdapter(savedDefinitionAdapter);
                binding.savedDefinitionRecyclerView.setLayoutManager(new LinearLayoutManager(DictionaryActivity.this));
            });
        });
    }


    /**
     * Prompts the user with a alert dialog to confirm if they want to delete the definition.
     * @param definition the definition to be deleted
     * @param position the position of the definition in the list
     */
    private void askDelDefinition(Definition definition, int position) {
        new AlertDialog.Builder(DictionaryActivity.this)
                .setTitle(getString(R.string.delete_definition, definition.getWord()))
                .setPositiveButton(R.string.yes, (dialog, which) -> delDefinition(definition, position))
                .setNegativeButton(R.string.no, (dialog, which) -> {
                })
                .show();
    }


    /**
     * Deletes the definition from the database.
     * @param definition the definition to be deleted
     * @param position the position of the definition in the list
     */
    private void delDefinition(Definition definition, int position) {
        savedDefinitions.remove(position);
        savedDefinitionAdapter.notifyDataSetChanged();
        Snackbar.make(binding.savedDefinitionRecyclerView, R.string.definition_deleted, Snackbar.LENGTH_SHORT).setAction(R.string.undo, v -> {
            if (deletedDefinitions != null && deletedDefinitions.get(0) != null) {
                savedDefinitions.add(position, deletedDefinitions.get(0));
                savedDefinitionAdapter.notifyDataSetChanged();
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


    /**
     * This class is the adapter class for the RecyclerView in DictionaryActivity.
     * It manages the population of definition items within the RecyclerView..
     */
    public class DefinitionAdapter extends RecyclerView.Adapter<DefinitionViewHolder> {

        /**
         * Constructor for DefinitionAdapter
         */
        public DefinitionAdapter() {
        }

        /**
         * This method is called when RecyclerView needs a new ViewHolder of the given type to represent an item.
         * @param parent The ViewGroup into which the new View will be added after it is bound to an adapter position.
         * @param viewType The view type of the new View.
         * @return A new DefinitionViewHolder that holds a View for the definition items.
         */
        @NonNull
        @Override
        public DefinitionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dict_word, parent, false);
            return new DefinitionViewHolder(view);
        }

        /**
         * This method is called when the RecyclerView needs to display the definition item at the specified position.
         * @param holder The ViewHolder which should be updated to represent the contents of the
         *               item at the given position in the data set.
         * @param position The position of the item within the adapter's data set.
         */
        @Override
        public void onBindViewHolder(DefinitionViewHolder holder, int position) {
            Definition definition = savedDefinitions.get(position);
            holder.bind(definition, position);
        }

        /**
         * This method returns the total number of items in the data set held by the adapter.
         * @return The total number of items in this adapter.
         */
        @Override
        public int getItemCount() {
            return savedDefinitions.size();
        }

    }

    /**
     * DefinitionViewHolder is the ViewHolder class for displaying a single definition within the RecyclerView.
     * It holds the layout for the individual dictionary entries.
     */
    public class DefinitionViewHolder extends RecyclerView.ViewHolder {

        /**
         * TextView for displaying the word
         */
        TextView wordTextView;

        /**
         * TextView for displaying the definition
         */
        TextView definitionTextView;


        /**
         * Constructor for DefinitionViewHolder
         * and binds the views to the layout
         * @param itemView The layout view for a single item in the list
         */
        public DefinitionViewHolder(View itemView) {
            super(itemView);
            wordTextView = itemView.findViewById(R.id.word_text_view);
            definitionTextView = itemView.findViewById(R.id.definition_text_view);
        }


        /**
         * Binds the definition data to the TextViews and sets up the click listener for
         * Viewing and deleting an item..
         * @param definition the definition to be displayed
         * @param position the position of the definition in the list
         */
        public void bind(Definition definition, int position) {
            wordTextView.setText(definition.getWord());
            definitionTextView.setText(definition.getDefinition());

            //click the saved word to view the definition(get data from database)
            itemView.setOnClickListener(v -> DictionaryResultActivity.launch(v.getContext(), definition.getWord(), false));
            //long click the saved word to ask user if they want to delete the definition
            itemView.setOnLongClickListener(v -> {
                askDelDefinition(definition, position);
                return false;
            });
        }

    }

}