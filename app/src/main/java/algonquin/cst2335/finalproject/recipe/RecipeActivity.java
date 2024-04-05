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
    private Database database; // Note the change here from DatabaseHelper to Database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        editTextRecipeSearch = findViewById(R.id.editText_recipe_search);
        buttonSearch = findViewById(R.id.button_search);
        recyclerViewRecipes = findViewById(R.id.recyclerView_recipes);

        database = new Database(this); // Initialize the Database instance

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
                recipes.add(new Recipe(0,title, image, summary, sourceUrl));
            }
        } catch ( JSONException e) {
            e.printStackTrace();
        }
        return recipes;
    }

    private void insertRecipeIntoDatabase(Recipe recipe) {
        database.insertRecipe(recipe.getTitle(), recipe.getImage(), recipe.getSummary(), recipe.getSourceUrl());
    }

    private void fetchRecipesFromDatabase() {
        List<Recipe> recipes = database.getAllRecipes();
        recipeAdapter.updateRecipes(recipes);
    }

    private void deleteRecipeFromDatabase(int id) {
        database.deleteRecipe(id);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        database.close(); // Close the database connection properly
    }
}
