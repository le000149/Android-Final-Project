package org.algonquin.cst2355.finalproject.dictionary;

/**
 * This is for the CST2355 Final Project - Dictionary Topic
 * Author: Yeqing Xia
 * Lab Section: CST2335 013
 * Creation Date: 2024/03/31
 */

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.algonquin.cst2355.finalproject.MainApplication;
import org.algonquin.cst2355.finalproject.R;
import org.algonquin.cst2355.finalproject.databinding.ActivityDictionaryResultBinding;
import org.algonquin.cst2355.finalproject.dictionary.model.Definition;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * This class displays dictionary search results.
 * It can do online search or fetch from local database
 * and save or delete definitions
 */
public class DictionaryResultActivity extends AppCompatActivity {

    /**
     * Key used in the Intent to store the searched word
     */
    public static final String WORD = "word";

    /**
     * key used in the Intent to indicate if the search should be done online
     */
    public static final String ONLINE_SEARCH = "online_search";

    /**
     * Base URL for the onlineDictionary API
     */
    public static final String BASE_URL = "https://api.dictionaryapi.dev/api/v2/entries/en/";

    /**
     * Key in JSON response for the meanings section.
     */
    public static final String MEANINGS = "meanings";

    /**
     * Key in the JSON response for the definitions within the meanings
     */
    public static final String DEFINITIONS = "definitions";

    /**
     * Key in the JSON response for a single definition
     */
    public static final String DEFINITION = "definition";
    private static final String TAG = "DictionaryResult";
    ActivityDictionaryResultBinding binding;

    private List<Definition> definitions;
    private String word;
    private boolean definitionSaved = false;

    /**
     * To launch the DictionaryResultActivity
     * @param context the context from where the activity is launched
     * @param word the word to search definitions for
     * @param onlineSearch flag indicating if the search should be done online
     */
    public static void launch(Context context, String word, boolean onlineSearch) {
        Intent intent = new Intent(context, DictionaryResultActivity.class);
        intent.putExtra(WORD, word);
        intent.putExtra(ONLINE_SEARCH, onlineSearch);
        context.startActivity(intent);
    }

    /**
     * Called when the activity is starting. Initializes the activity, inflates its UI
     * and triggers the definition search process
     * @param savedInstanceState If the activity is being re-initialized after previously being
     *                           shut down then this Bundle contains the data it most recently
     *                           supplied, Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        binding = ActivityDictionaryResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        word = getIntent().getStringExtra(WORD);
        getSupportActionBar().setTitle(word);

        boolean onlineSearch = getIntent().getBooleanExtra(ONLINE_SEARCH, false);
        updateSaveOrDeleteIcon();
        if (onlineSearch) {
            searchDefinitionOnline(word);
        } else {
            searchDefinitionFromDataBase(word);
        }
    }

    /**
     * Initialize the menu with the save or delete option
     * @param menu The options menu which will be populated.
     * @return Return true for the menu to be displayed; if return false it will not be shown.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dictionary_result, menu);
        MenuItem item = menu.findItem(R.id.save_or_delete_definition);
        item.setIcon(definitionSaved ? R.drawable.delete : R.drawable.bookmark_add); //Changes the icon depending on whether the word is saved or not
        return true;
    }


    /**
     * This method is called whenever an item in options menu is selected.
     * @param item The menu item that was selected.
     * @return Returns true to consume it here, false to pass it on.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.save_or_delete_definition) {
            if (definitions != null && !definitions.isEmpty()) {
                if (definitionSaved) {
                    deleteDefinitions(word);
                    definitionSaved = false;
                    item.setIcon(R.drawable.bookmark_add);
                    Toast.makeText(this, R.string.definition_deleted, Toast.LENGTH_SHORT).show();
                } else {
                    saveDefinitions(definitions);
                    definitionSaved = true;
                    item.setIcon(R.drawable.delete);
                    Toast.makeText(this, R.string.definition_saved, Toast.LENGTH_SHORT).show();
                }
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * This method is called when the user requests that the Activity navigate up, here is the place to go back
     * @return Returns true to indicate that the event has been handled.
     */
    @Override
    public boolean onSupportNavigateUp() {
        getOnBackPressedDispatcher().onBackPressed();
        return true;
    }

    /**
     * This method updates the save or delete icon depending on whether the definition
     * is saved in the database or not
     */
    private void updateSaveOrDeleteIcon() {
        Executors.newSingleThreadExecutor().execute(() -> {
            definitionSaved = !MainApplication.getDictionaryDB().DefinitionDao().getDefinitions(word).isEmpty();
            if (definitionSaved) {
                runOnUiThread(() -> invalidateOptionsMenu());
            }
        });
    }

    /**
     * To delete the saved definitions of a word from the database
     * @param word The word to be deleted.
     */
    private void deleteDefinitions(String word) {
        Executors.newSingleThreadExecutor().execute(() -> MainApplication.getDictionaryDB().DefinitionDao().deleteDefinition(word));
    }

    /**
     * To save the definitions of a word into the database
     * @param definitions The list of definitions to be saved.
     */
    private void saveDefinitions(List<Definition> definitions) {
        Log.d(TAG, "saveDefinitions: ");
        Executors.newSingleThreadExecutor().execute(() -> MainApplication.getDictionaryDB().DefinitionDao().saveDefinitions(definitions));
    }


    /**
     * search for the word on the internet
     * @param word
     */
    private void searchDefinitionOnline(String word) {
        Log.d(TAG, "searchDefinitionOnline: " + word);
        Log.d(TAG, "searchForTerm: " + word); //for debug purposes
        String url = BASE_URL + word;

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            try {
                definitions = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    JSONObject wordObject = response.getJSONObject(i);
                    JSONArray phonetics = wordObject.getJSONArray("phonetics");
                    for (int k = 0; k < phonetics.length(); k++) {
                        JSONObject phonetic = phonetics.getJSONObject(k);
                        if (phonetic.has("text") && phonetic.has("audio")) {
                            definitions.add(new Definition(word, null, phonetic.getString("text"), phonetic.getString("audio"), null));
                        }
                    }
                    JSONArray meaningsArray = wordObject.getJSONArray(MEANINGS);
                    for (int j = 0; j < meaningsArray.length(); j++) {
                        JSONObject meaningsObject = meaningsArray.getJSONObject(j);
                        if (meaningsObject.has("partOfSpeech")) {
                            definitions.add(new Definition(word, null, null, null, meaningsObject.getString("partOfSpeech")));
                        }
                        JSONArray definitionsArray = meaningsObject.getJSONArray(DEFINITIONS);
                        for (int k = 0; k < definitionsArray.length(); k++) {
                            JSONObject definitionObject = definitionsArray.getJSONObject(k);
                            String definition = definitionObject.getString(DEFINITION);
                            definitions.add(new Definition(word, definition, null, null, null));
                        }
                    }
                }
                updateRecyclerView(definitions);
            } catch (JSONException e) {
                Log.e(TAG, "searchDefinitionOnline: ", e);
                Toast.makeText(DictionaryResultActivity.this, getString(R.string.search_failed, e.getMessage()), Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            Toast.makeText(DictionaryResultActivity.this, getString(R.string.search_failed, error.getMessage()), Toast.LENGTH_SHORT).show();
        });

        queue.add(jsonArrayRequest);
    }

    /**
     * search for the word in the database
     * @param word
     */
    private void searchDefinitionFromDataBase(String word) {
        Log.d(TAG, "searchDefinitionFromDataBase: " + word);
        Executors.newSingleThreadExecutor().execute(() -> {
            definitions = MainApplication.getDictionaryDB().DefinitionDao().getDefinitions(word);
            runOnUiThread(() -> updateRecyclerView(definitions));
        });
    }

    /**
     * update the recycler view
     * @param wordDefinitions
     */
    private void updateRecyclerView(List<Definition> wordDefinitions) {
        DefinitionAdapter adapter = new DefinitionAdapter(wordDefinitions);
        binding.definitionRecyclerView.setAdapter(adapter);
        binding.definitionRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * This is a adapter class for the RecyclerView that displays a list of definitions
     */
    class DefinitionAdapter extends RecyclerView.Adapter<DefinitionViewHolder> {
        /**
         * a list of definition objects to be displayed
         */
        private final List<Definition> definitions;

        /**
         * constructor for DefinitionAdapter
         * @param definitions
         */
        public DefinitionAdapter(List<Definition> definitions) {
            Log.d(TAG, "DefinitionAdapter: ");
            this.definitions = definitions;
        }


        /**
         * Create and return a new ViewHolder instance
         * @param parent The ViewGroup inflating the view
         * @param viewType The view type of the new View.
         * @return A new DefinitionViewHolder instance
         */
        @NonNull
        @Override
        public DefinitionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Log.d(TAG, "onCreateViewHolder: ");
            View view;
            int layoutId = R.layout.item_dict_definition;
            if (viewType == 1) {
                layoutId = R.layout.item_dict_audio;
            } else if (viewType == 2) {
                layoutId = R.layout.item_dict_pos;
            }
            view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
            return new DefinitionViewHolder(view);
        }

        /**
         * Binds data from a Definition object to the DefinitionViewHolder at a given position.
         * @param holder The ViewHolder which should be updated to represent the contents of the
         *        item at the given position in the data set.
         * @param position The position of the item within the adapter's data set.
         */
        @Override
        public void onBindViewHolder(@NonNull DefinitionViewHolder holder, int position) {
            Log.d(TAG, "onBindViewHolder: " + position);
            Definition definition = definitions.get(position);
            holder.bind(definition);
        }

        /**
         * Returns the total number of items in the adapter
         * @return size of definitions
         */
        @Override
        public int getItemCount() {
            return definitions.size();
        }

        @Override
        public int getItemViewType(int position) {
            Definition definition = definitions.get(position);
            if (definition.getAudio() != null) {
                return 1;
            } else if (definition.getPartOfSpeech() != null) {
                return 2;
            } else {
                return 0;
            }
        }
    }

    /**
     * This is a ViewHolder class for the RecyclerView that displays a list of definitions
     */
    class DefinitionViewHolder extends RecyclerView.ViewHolder {
        /**
         * TextView that displays the definition text
         */
        private final TextView wordTextView;
        private final TextView wordAudio;
        private final View iconSpeak;

        /**
         * Constructor for DefinitionViewHolder
         * @param itemView The view representing a single definition item
         */
        public DefinitionViewHolder(@NonNull View itemView) {
            super(itemView);
            wordTextView = itemView.findViewById(R.id.definition_text_view);
            wordAudio = itemView.findViewById(R.id.definition_audio);
            iconSpeak = itemView.findViewById(R.id.speak);
        }

        /**
         * Binds the data from a Definition object to the DefinitionViewHolder
         * @param definition The Definition object containing the definition text
         */
        public void bind(Definition definition) {
            if (definition.getDefinition() != null) {
                wordTextView.setText(definition.getDefinition());
            }

            if (definition.getPartOfSpeech() != null) {
                wordTextView.setText(definition.getPartOfSpeech());
            }

            if (definition.getPhonetic() != null) {
                wordAudio.setText(definition.getPhonetic());
                if (definition.getAudio() != null && !definition.getAudio().isEmpty()) {
                    itemView.setOnClickListener(v -> {
                        try {
                            Log.d(TAG, "play audio: " + definition.getAudio());
                            MediaPlayer mediaPlayer = new MediaPlayer();
                            mediaPlayer.setDataSource(definition.getAudio());
                            mediaPlayer.prepareAsync();
                            mediaPlayer.setOnPreparedListener(mp -> {
                                mp.start();
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                } else {
                    iconSpeak.setVisibility(View.GONE);
                }
            }
        }
    }

}