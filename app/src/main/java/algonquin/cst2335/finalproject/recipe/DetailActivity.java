package algonquin.cst2335.finalproject.recipe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;

import org.json.JSONException;

import algonquin.cst2335.finalproject.MyApplication;
import algonquin.cst2335.finalproject.R;

/**
 * Activity for displaying detailed information about a recipe.
 */
public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        // Using -1 as default to check for "no value passed" condition
        int recipeId = intent.getIntExtra("recipeId", -1);

        if (recipeId <= 0) {
            recipeId = 1; // Default ID, change this to a valid, meaningful default if possible
        }
        fetchRecipeDetails(recipeId);
        Button saveToFavoritesButton = findViewById(R.id.button_save_to_favorites);
        saveToFavoritesButton.setOnClickListener(v -> saveRecipeToFavorites());
    }


    private void saveRecipeToFavorites() {
        // Example: Saving the recipe title to SharedPreferences as a placeholder
        // You might want to save more details or use a database for more complex data
        SharedPreferences sharedPreferences = getSharedPreferences("Favorites", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        TextView titleView = findViewById(R.id.textView_recipe_title);
        String recipeTitle = titleView.getText().toString();
        editor.putString("favoriteRecipe", recipeTitle);
        editor.apply();

        Toast.makeText(this, "Recipe saved to favorites", Toast.LENGTH_SHORT).show();
    }
    /**
     * Fetches detailed information about a recipe from the API.
     * @param recipeId The ID of the recipe to fetch details for.
     */
    private void fetchRecipeDetails(int recipeId) {
        String apiKey = "4278cf074b6b4bf7b532b45ecf70a45d";
        String url = "https://api.spoonacular.com/recipes/" + recipeId + "/information?apiKey=" + apiKey;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    Log.d("API Response", response.toString());
                    try {
                        String title = response.getString("title");
                        String image = response.getString("image");
                        String summary = response.getString("summary");
                        String sourceUrl = response.getString("sourceUrl");

                        runOnUiThread(() -> updateUI(title, image, summary, sourceUrl));
                    } catch (JSONException e) {
                        Log.e("JSON Parsing Error", e.getMessage());
                    }
                },
                error -> Log.e("Volley Error", error.toString())
        );

        MyApplication.getRequestQueue().add(jsonObjectRequest);
    }

    /**
     * Updates the UI with the fetched recipe details.
     * @param title The title of the recipe.
     * @param image The URL of the recipe image.
     * @param summary The summary of the recipe.
     * @param sourceUrl The source URL of the recipe.
     */
    private void updateUI(String title, String image, String summary, String sourceUrl) {
        TextView titleView = findViewById(R.id.textView_recipe_title);
        ImageView imageView = findViewById(R.id.imageView_recipe_image);
        TextView summaryView = findViewById(R.id.textView_recipe_summary);
        TextView sourceUrlView = findViewById(R.id.textView_source_url);

        titleView.setText(title);
        Glide.with(this).load(image).into(imageView);
        summaryView.setText(summary);
        sourceUrlView.setText(sourceUrl);
    }
}
