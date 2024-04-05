package algonquin.cst2335.finalproject.recipe;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;
import algonquin.cst2335.finalproject.R;

/**
 * Adapter for displaying a list of recipes in a RecyclerView.
 */
public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    private final List<Recipe> recipeList;

    /**
     * Constructs the RecipeAdapter with a list of recipes.
     *
     * @param recipeList List of Recipe objects to be displayed.
     */
    public RecipeAdapter(List<Recipe> recipeList) {
        this.recipeList = recipeList;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipeList.get(position);
        holder.bind(recipe);
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    /**
     * Updates the list of recipes in the adapter and refreshes the view.
     *
     * @param newRecipes New list of Recipe objects to display.
     */
    public void updateRecipes(List<Recipe> newRecipes) {
        recipeList.clear();
        recipeList.addAll(newRecipes);
        notifyDataSetChanged();
    }

    /**
     * ViewHolder class for recipe items in the list.
     */
    public class RecipeViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewRecipeTitle;
        private final ImageView imageViewRecipeImage;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewRecipeTitle = itemView.findViewById(R.id.textView_recipe_title);
            imageViewRecipeImage = itemView.findViewById(R.id.imageView_recipe_image);
        }

        /**
         * Binds a Recipe object to the ViewHolder, setting up the displayed title and image, and handling click events.
         *
         * @param recipe The Recipe object to be displayed by this ViewHolder.
         */
        public void bind(Recipe recipe) {
            textViewRecipeTitle.setText(recipe.getTitle());
            Glide.with(itemView.getContext()).load(recipe.getImage()).into(imageViewRecipeImage);
            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(itemView.getContext(), DetailActivity.class);
                // Ensure recipe.getId() is correctly returning an ID, not defaulting to 0
                intent.putExtra("recipeId", recipe.getId());
                itemView.getContext().startActivity(intent);
            });
        }
    }
}
