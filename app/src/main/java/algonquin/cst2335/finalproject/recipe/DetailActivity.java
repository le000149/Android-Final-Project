package algonquin.cst2335.finalproject.recipe;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;

import org.json.JSONException;

import algonquin.cst2335.finalproject.MyApplication;
import algonquin.cst2335.finalproject.R;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        int recipeId = intent.getIntExtra("recipeId", -1);

        if (recipeId != -1) {
            // Fetch detailed information using the recipe ID
            fetchRecipeDetails(recipeId);
        }
    }

    private void fetchRecipeDetails(int recipeId) {
        String apiKey = "4278cf074b6b4bf7b532b45ecf70a45d";
        String url = "https://api.spoonacular.com/recipes/" + recipeId + "/information?apiKey=" + apiKey;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    Log.d("API Response", response.toString()); // Add this line to log the response
                    try {
                        String title = response.getString("title");
                        String image = response.getString("image");
                        String summary = response.getString("summary");
                        String sourceUrl = response.getString("sourceUrl");

                        runOnUiThread(() -> updateUI(title, image, summary, sourceUrl));
                    } catch (JSONException e) {
                        Log.e("JSON Parsing Error", e.getMessage()); // Improve error logging
                    }
                },
                error -> Log.e("Volley Error", error.toString()) // Log errors
        );

        MyApplication.getRequestQueue().add(jsonObjectRequest);

    }

    // Method to update UI with fetched recipe details
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
