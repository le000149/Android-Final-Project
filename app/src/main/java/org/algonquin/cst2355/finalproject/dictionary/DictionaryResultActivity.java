package org.algonquin.cst2355.finalproject.dictionary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.algonquin.cst2355.finalproject.R;
import org.algonquin.cst2355.finalproject.databinding.ActivityDictionaryResultBinding;
import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class DictionaryResultActivity extends AppCompatActivity {

    public static final String WORD = "word";

    ActivityDictionaryResultBinding binding;

    public static void launch(Activity activity, String word) {
        Intent intent = new Intent(activity, DictionaryResultActivity.class);
        intent.putExtra(WORD, word);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDictionaryResultBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        String word = getIntent().getStringExtra(WORD);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(word);
        }
        searchForTerm(word);
    }

    private void searchForTerm(String term) {
        String url = "https://api.dictionaryapi.dev/api/v2/entries/en/" + term;

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Gson gson = new Gson();
                Type listType = new TypeToken<List<Word>>() {
                }.getType();
                List<Word> words = gson.fromJson(response.toString(), listType);
                updateRecyclerView(words);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error
            }
        });

        queue.add(jsonArrayRequest);
    }

    private void updateRecyclerView(List<Word> words) {
        WordDefinitionAdapter adapter = new WordDefinitionAdapter(words);
        binding.definitionRecyclerView.setAdapter(adapter);
        binding.definitionRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    static class WordDefinitionAdapter extends RecyclerView.Adapter<WordDefinitionViewHolder> {
        private final List<Word> words;
        private final List<Word.Definition> definitions;

        public WordDefinitionAdapter(List<Word> words) {
            this.words = words;
            definitions = new ArrayList<>();
            for (Word definition : words) {
                for (Word.Meaning meaning : definition.getMeanings()) {
                    definitions.addAll(meaning.getDefinitions());
                }
            }
        }

        @NonNull
        @Override
        public WordDefinitionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dict_word_definition, parent, false);
            return new WordDefinitionViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull WordDefinitionViewHolder holder, int position) {
            Word.Definition definition = definitions.get(position);
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

        public void bind(Word.Definition meaning) {
            wordTextView.setText(meaning.getDefinition());
        }
    }

}