package algonquin.cst2335.finalproject.recipe;
// RecipeAdapter.java
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
public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    private List<Recipe> recipeList;

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

    public void updateRecipes(List<Recipe> newRecipes) {
        recipeList.clear();
        recipeList.addAll(newRecipes);
        notifyDataSetChanged();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewRecipeTitle;
        private ImageView imageViewRecipeImage;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewRecipeTitle = itemView.findViewById(R.id.textView_recipe_title);
            imageViewRecipeImage = itemView.findViewById(R.id.imageView_recipe_image);
        }

        public void bind(Recipe recipe) {
            textViewRecipeTitle.setText(recipe.getTitle());
            Glide.with(itemView.getContext()).load(recipe.getImage()).into(imageViewRecipeImage);
            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(itemView.getContext(), DetailActivity.class);
                intent.putExtra("recipeId", recipe.getId()); // Pass the recipe ID
                itemView.getContext().startActivity(intent);
            });
        }

    }
}
