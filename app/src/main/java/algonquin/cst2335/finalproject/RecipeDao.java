package algonquin.cst2335.finalproject;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RecipeDao {
    @Insert
    void insertRecipe(Recipe recipe);

    @Delete
    void deleteRecipe(Recipe recipe);

    @Query("SELECT * FROM recipes")
    List<Recipe> getAllRecipes();
}
