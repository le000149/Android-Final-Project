package org.algonquin.cst2355.finalproject.dictionary;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import org.algonquin.cst2355.finalproject.databinding.ActivityDictionaryResultBinding;


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
    }
}