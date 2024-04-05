package algonquin.cst2335.finalproject.recipe;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

import algonquin.cst2335.finalproject.R;

public class FavoritesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecipeAdapter adapter;
    private List<Recipe> favoriteRecipes = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        recyclerView = findViewById(R.id.recyclerView_favorites);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecipeAdapter(favoriteRecipes);
        recyclerView.setAdapter(adapter);

        loadFavorites();
    }

    private void loadFavorites() {
        SharedPreferences prefs = getSharedPreferences("Favorites", MODE_PRIVATE);
        // This is a simplified way. You should parse a list stored in a more suitable format.
        String title = prefs.getString("favoriteRecipeTitle", null);
        if (title != null) {
            Recipe recipe = new Recipe(title, "", "Summary placeholder", "URL placeholder");
            favoriteRecipes.add(recipe);
            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, "No favorites saved", Toast.LENGTH_SHORT).show();
        }
    }
}
