package algonquin.cst2335.finalproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class RecipeDetailsActivity extends AppCompatActivity {
    private ImageView imageView;
    private TextView summaryTextView;
    private TextView sourceUrlTextView;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        imageView = findViewById(R.id.recipe_image_view);
        summaryTextView = findViewById(R.id.summary_text_view);
        sourceUrlTextView = findViewById(R.id.source_url_text_view);
        saveButton = findViewById(R.id.save_button);

        // Retrieve recipe details from Intent
        Recipe recipe = getIntent().getParcelableExtra("recipe");
        if (recipe != null) {
            // Display recipe details
            Glide.with(this).load(recipe.getImageUrl()).into(imageView);
            summaryTextView.setText(recipe.getSummary());
            sourceUrlTextView.setText(recipe.getSourceUrl());
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save recipe to database
                // You need to implement this method
            }
        });
    }
}
