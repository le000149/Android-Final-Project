package algonquin.cst2335.finalproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private EditText searchEditText;
    private Button searchButton;
    private RecyclerView recyclerView;
    private RecipeAdapter recipeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchEditText = findViewById(R.id.search_edit_text);
        searchButton = findViewById(R.id.search_button);
        recyclerView = findViewById(R.id.recycler_view);

        // Initialize RecyclerView and Adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recipeAdapter = new RecipeAdapter(new ArrayList<>(), new RecipeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Recipe recipe) {
                // Handle item click
                showRecipeDetails(recipe);
            }
        });
        recyclerView.setAdapter(recipeAdapter);

        // Load saved search term from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String savedSearchTerm = sharedPreferences.getString("searchTerm", "");
        searchEditText.setText(savedSearchTerm);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchTerm = searchEditText.getText().toString().trim();
                if (!searchTerm.isEmpty()) {
                    // Save search term to SharedPreferences
                    sharedPreferences.edit().putString("searchTerm", searchTerm).apply();
                    // Perform search operation
                    performSearch(searchTerm);
                } else {
                    Toast.makeText(MainActivity.this, "Please enter a search term", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void performSearch(String searchTerm) {
        // Call API and handle response to update RecyclerView
        // You need to implement this method using Retrofit, Volley, or any other networking library
    }

    private void showRecipeDetails(Recipe recipe) {
        Intent intent = new Intent(MainActivity.this, RecipeDetailsActivity.class);
        intent.putExtra("recipe", recipe.toString());
        startActivity(intent);
    }
}
