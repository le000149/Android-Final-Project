package algonquin.cst2335.finalproject.recipe;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "recipes.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_RECIPES = "recipes";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_IMAGE = "image";
    private static final String COLUMN_SUMMARY = "summary";
    private static final String COLUMN_SOURCE_URL = "sourceUrl";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_RECIPES_TABLE = "CREATE TABLE " + TABLE_RECIPES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_IMAGE + " TEXT,"
                + COLUMN_SUMMARY + " TEXT,"
                + COLUMN_SOURCE_URL + " TEXT" + ")";
        db.execSQL(CREATE_RECIPES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        onCreate(db);
    }

    /**
     * Insert a new recipe into the database.
     */
    public void insertRecipe(String title, String image, String summary, String sourceUrl) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_IMAGE, image);
        values.put(COLUMN_SUMMARY, summary);
        values.put(COLUMN_SOURCE_URL, sourceUrl);

        db.insert(TABLE_RECIPES, null, values);
        db.close();
    }

    /**
     * Fetch all recipes from the database.
     */
    public List<Recipe> getAllRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_RECIPES, new String[]{COLUMN_ID, COLUMN_TITLE, COLUMN_IMAGE, COLUMN_SUMMARY, COLUMN_SOURCE_URL},
                null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                String title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));
                String image = cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE));
                String summary = cursor.getString(cursor.getColumnIndex(COLUMN_SUMMARY));
                String sourceUrl = cursor.getString(cursor.getColumnIndex(COLUMN_SOURCE_URL));

                Recipe recipe = new Recipe(id, title, image, summary, sourceUrl); // Assuming a constructor that includes id
                recipes.add(recipe);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return recipes;
    }


    /**
     * Delete a recipe from the database by its ID.
     */
    public void deleteRecipe(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RECIPES, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }
}
