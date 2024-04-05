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

/**
 * Activity for searching and displaying a list of recipes.
 */
public class RecipeActivity extends AppCompatActivity {
    private EditText editTextRecipeSearch;
    private Button buttonSearch;
    private RecyclerView recyclerViewRecipes;
    private RecipeAdapter recipeAdapter;
    private Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        editTextRecipeSearch = findViewById(R.id.editText_recipe_search);
        buttonSearch = findViewById(R.id.button_search);
        recyclerViewRecipes = findViewById(R.id.recyclerView_recipes);

        database = new Database(this);

        recyclerViewRecipes.setLayoutManager(new LinearLayoutManager(this));
        recipeAdapter = new RecipeAdapter(new ArrayList<>());
        recyclerViewRecipes.setAdapter(recipeAdapter);

        buttonSearch.setOnClickListener(v -> {
            String query = editTextRecipeSearch.getText().toString().trim();
            if (!query.isEmpty()) {
                fetchRecipes(query);
            } else {
                Toast.makeText(RecipeActivity.this, "Please enter a recipe name", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Fetches recipes from the API based on the search query.
     * @param query The search query for recipes.
     */
    private void fetchRecipes(String query) {
        String apiKey = "4278cf074b6b4bf7b532b45ecf70a45d";
        String url = "https://api.spoonacular.com/recipes/complexSearch?query=" + query + "&apiKey=" + apiKey;

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    List<Recipe> recipes = parseRecipes(response);
                    recipeAdapter.updateRecipes(recipes);
                    recipes.forEach(this::insertRecipeIntoDatabase);
                },
                error -> Toast.makeText(RecipeActivity.this, "Failed to fetch recipes", Toast.LENGTH_SHORT).show());

        queue.add(stringRequest);
    }

    /**
     * Parses the JSON response from the API into a list of Recipe objects.
     * @param jsonResponse The JSON response string from the API.
     * @return A list of Recipe objects.
     */
    private List<Recipe> parseRecipes(String jsonResponse) {
        List<Recipe> recipes = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray results = jsonObject.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject recipeObject = results.getJSONObject(i);
                String title = recipeObject.getString("title");
                String image = recipeObject.getString("image");
                // Assuming summary and sourceUrl are fetched here or set as placeholders
                String summary = "Summary not available"; // Placeholder value
                String sourceUrl = "URL not available"; // Placeholder value
                recipes.add(new Recipe(title, image, summary, sourceUrl));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return recipes;
    }

    /**
     * Inserts a Recipe object into the local database.
     * @param recipe The Recipe object to insert.
     */
    private void insertRecipeIntoDatabase(Recipe recipe) {
        database.insertRecipe(recipe.getTitle(), recipe.getImage(), recipe.getSummary(), recipe.getSourceUrl());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        database.close();
    }
}
