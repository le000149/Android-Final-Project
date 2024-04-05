package algonquin.cst2335.finalproject.recipe;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import algonquin.cst2335.finalproject.R;

public class RecipeActivity extends AppCompatActivity {
    private EditText editTextRecipeSearch;
    private Button buttonSearch;
    private RecyclerView recyclerViewRecipes;
    private RecipeAdapter recipeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        // Initialize views
        editTextRecipeSearch = findViewById(R.id.editText_recipe_search);
        buttonSearch = findViewById(R.id.button_search);
        recyclerViewRecipes = findViewById(R.id.recyclerView_recipes);

        // Initialize RecyclerView
        recyclerViewRecipes.setLayoutManager(new LinearLayoutManager(this));
        recipeAdapter = new RecipeAdapter(new ArrayList<Recipe>());
        recyclerViewRecipes.setAdapter(recipeAdapter);

        // Set click listener for the search button
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = editTextRecipeSearch.getText().toString();
                if (!query.isEmpty()) {
                    fetchRecipes(query);
                } else {
                    Toast.makeText(RecipeActivity.this, "Please enter a recipe name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Method to fetch recipes from the server
    private void fetchRecipes(String query) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String apiKey = "4278cf074b6b4bf7b532b45ecf70a45d";
        String url = "https://api.spoonacular.com/recipes/complexSearch?query=" + query + "&apiKey=" + apiKey;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    List<Recipe> recipes = parseRecipes(response);
                    recipeAdapter.updateRecipes(recipes);
                },
                error -> Toast.makeText(RecipeActivity.this, "That didn't work!", Toast.LENGTH_SHORT).show());

        queue.add(stringRequest);
    }

    private List<Recipe> parseRecipes(String jsonResponse) {
        List<Recipe> recipes = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject recipeObject = jsonArray.getJSONObject(i);
                String title = recipeObject.getString("title");
                String id = recipeObject.getString("id"); // This might be used for a detailed fetch later
                String image = recipeObject.getString("image");
                // Placeholder values for summary and sourceUrl since they are not available now
                String summary = "Summary not available"; // Placeholder
                String sourceUrl = "URL not available"; // Placeholder
                Recipe recipe = new Recipe(title, image, summary, sourceUrl);
                recipes.add(recipe);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return recipes;
    }

}
