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
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.algonquin.cst2355.finalproject.MainApplication;
import org.algonquin.cst2355.finalproject.R;
import org.algonquin.cst2355.finalproject.databinding.ActivityDictionaryResultBinding;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;


public class DictionaryResultActivity extends AppCompatActivity {
    public static final String WORD = "word";
    public static final String ONLINE_SEARCH = "online_search";
    private static final String TAG = "DictionaryResult";

    ActivityDictionaryResultBinding binding;

    private List<Definition> definitions;
    private String word;
    private boolean definitionSaved = true;

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

        word = getIntent().getStringExtra(WORD);
        binding.toolbar.setTitle(word);
        searchDefinitionOnline(word);
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
                    Toast.makeText(this, "Definition deleted", Toast.LENGTH_SHORT).show();
                } else {
                    saveDefinitions(definitions);
                    definitionSaved = true;
                    item.setIcon(R.drawable.delete);
                    Toast.makeText(this, "Definition saved", Toast.LENGTH_SHORT).show();
                }
            }
        }
        return true;
    }

    private void updateSaveOrDeleteIcon() {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                definitionSaved = MainApplication.getDictionaryDB().DefinitionDao().getDefinitions(word).size() > 0;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        invalidateOptionsMenu();
                    }
                });
            }
        });
    }

    private void deleteDefinitions(String word) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                MainApplication.getDictionaryDB().DefinitionDao().deleteDefinition(word);
            }
        });
    }

    private void saveDefinitions(List<Definition> definitions) {
        Log.d(TAG, "saveDefinitions: ");
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                MainApplication.getDictionaryDB().DefinitionDao().saveDefinitions(definitions);
            }
        });
    }

    private void searchDefinitionOnline(String word) {
        Log.d(TAG, "searchForTerm: " + word);
        String url = "https://api.dictionaryapi.dev/api/v2/entries/en/" + word;

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    definitions = new ArrayList<>();
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject wordObject = response.getJSONObject(i);
                        JSONArray meaningsArray = wordObject.getJSONArray("meanings");
                        for (int j = 0; j < meaningsArray.length(); j++) {
                            JSONObject meaningsObject = meaningsArray.getJSONObject(j);
                            JSONArray definitionsArray = meaningsObject.getJSONArray("definitions");
                            for (int k = 0; k < definitionsArray.length(); k++) {
                                JSONObject definitionObject = definitionsArray.getJSONObject(k);
                                String definition = definitionObject.getString("definition");
                                definitions.add(new Definition(word, definition));
                            }
                        }
                    }
                    updateRecyclerView(definitions);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error
            }
        });

        queue.add(jsonArrayRequest);
    }


    private void searchDefinitionFromDataBase(String word) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                definitions = MainApplication.getDictionaryDB().DefinitionDao().getDefinitions(word);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateRecyclerView(definitions);
                    }
                });
            }
        });
    }

    private void updateRecyclerView(List<Definition> wordDefinitions) {
        WordDefinitionAdapter adapter = new WordDefinitionAdapter(wordDefinitions);
        binding.definitionRecyclerView.setAdapter(adapter);
        binding.definitionRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    static class WordDefinitionAdapter extends RecyclerView.Adapter<WordDefinitionViewHolder> {
        private final List<Definition> definitions;

        public WordDefinitionAdapter(List<Definition> definitions) {
            this.definitions = definitions;
        }

        @NonNull
        @Override
        public WordDefinitionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dict_word_definition, parent, false);
            return new WordDefinitionViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull WordDefinitionViewHolder holder, int position) {
            Definition definition = definitions.get(position);
            holder.bind(definition);
        }

        @Override
        public int getItemCount() {
            return definitions.size();
        }

    }

    static class WordDefinitionViewHolder extends RecyclerView.ViewHolder {
        private final TextView wordTextView;

        public WordDefinitionViewHolder(@NonNull View itemView) {
            super(itemView);
            wordTextView = itemView.findViewById(R.id.wordTextView);
        }

        public void bind(Definition definition) {
            wordTextView.setText(definition.getDefinition());
        }
    }

}