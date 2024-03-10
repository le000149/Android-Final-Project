package org.algonquin.cst2355.finalproject.dictionary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.algonquin.cst2355.finalproject.databinding.ActivityDictionaryResultBinding;
import org.json.JSONArray;


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
                binding.wordTextView.setText(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error
            }
        });
        queue.add(jsonArrayRequest);
    }

}