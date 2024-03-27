package org.algonquin.cst2355.finalproject.dictionary;

import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;


public class DictionaryResultActivity extends AppCompatActivity {
    public static final String WORD = "word";
    public static final String ONLINE_SEARCH = "online_search";
    public static final String BASE_URL = "https://api.dictionaryapi.dev/api/v2/entries/en/";
    public static final String MEANINGS = "meanings";
    public static final String DEFINITIONS = "definitions";
    public static final String DEFINITION = "definition";
    private static final String TAG = "DictionaryResult";
    ActivityDictionaryResultBinding binding;

    private List<Definition> definitions;
    private String word;
    private boolean definitionSaved = false;

    public static void launch(Context context, String word, boolean onlineSearch) {
        Intent intent = new Intent(context, DictionaryResultActivity.class);
        intent.putExtra(WORD, word);
        intent.putExtra(ONLINE_SEARCH, onlineSearch);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dictionary_result, menu);
        MenuItem item = menu.findItem(R.id.save_or_delete_definition);
        item.setIcon(definitionSaved ? R.drawable.delete : R.drawable.bookmark_add);
        return true;
    }

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

    @Override
    public boolean onSupportNavigateUp() {
        getOnBackPressedDispatcher().onBackPressed();
        return true;
    }

    private void updateSaveOrDeleteIcon() {
        Executors.newSingleThreadExecutor().execute(() -> {
            definitionSaved = !MainApplication.getDictionaryDB().DefinitionDao().getDefinitions(word).isEmpty();
            if (definitionSaved) {
                runOnUiThread(() -> invalidateOptionsMenu());
            }
        });
    }

    private void deleteDefinitions(String word) {
        Executors.newSingleThreadExecutor().execute(() -> MainApplication.getDictionaryDB().DefinitionDao().deleteDefinition(word));
    }

    private void saveDefinitions(List<Definition> definitions) {
        Log.d(TAG, "saveDefinitions: ");
        Executors.newSingleThreadExecutor().execute(() -> MainApplication.getDictionaryDB().DefinitionDao().saveDefinitions(definitions));
    }

    private void searchDefinitionOnline(String word) {
        Log.d(TAG, "searchForTerm: " + word);
        String url = BASE_URL + word;

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            try {
                definitions = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    JSONObject wordObject = response.getJSONObject(i);
                    JSONArray meaningsArray = wordObject.getJSONArray(MEANINGS);
                    for (int j = 0; j < meaningsArray.length(); j++) {
                        JSONObject meaningsObject = meaningsArray.getJSONObject(j);
                        JSONArray definitionsArray = meaningsObject.getJSONArray(DEFINITIONS);
                        for (int k = 0; k < definitionsArray.length(); k++) {
                            JSONObject definitionObject = definitionsArray.getJSONObject(k);
                            String definition = definitionObject.getString(DEFINITION);
                            definitions.add(new Definition(word, definition));
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

    private void searchDefinitionFromDataBase(String word) {
        Executors.newSingleThreadExecutor().execute(() -> {
            definitions = MainApplication.getDictionaryDB().DefinitionDao().getDefinitions(word);
            runOnUiThread(() -> updateRecyclerView(definitions));
        });
    }

    private void updateRecyclerView(List<Definition> wordDefinitions) {
        DefinitionAdapter adapter = new DefinitionAdapter(wordDefinitions);
        binding.definitionRecyclerView.setAdapter(adapter);
        binding.definitionRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    static class DefinitionAdapter extends RecyclerView.Adapter<DefinitionViewHolder> {
        private final List<Definition> definitions;

        public DefinitionAdapter(List<Definition> definitions) {
            this.definitions = definitions;
        }

        @NonNull
        @Override
        public DefinitionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dict_definition, parent, false);
            return new DefinitionViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull DefinitionViewHolder holder, int position) {
            Definition definition = definitions.get(position);
            holder.bind(definition);
        }

        @Override
        public int getItemCount() {
            return definitions.size();
        }

    }

    static class DefinitionViewHolder extends RecyclerView.ViewHolder {
        private final TextView wordTextView;

        public DefinitionViewHolder(@NonNull View itemView) {
            super(itemView);
            wordTextView = itemView.findViewById(R.id.definition_text_view);
        }

        public void bind(Definition definition) {
            wordTextView.setText(definition.getDefinition());
        }
    }

}