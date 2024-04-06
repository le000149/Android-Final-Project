package algonquin.cst2335.finalproject.recipe;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Provides access to a local SQLite database for storing and managing recipe data.
 * This class assists in creating the database if it doesn't exist, opening it if it does,
 * and upgrading it as necessary for new versions of your app.
 */
public class MyDBOpenHelper extends SQLiteOpenHelper {

    /**
     * Name of the database file.
     */
    private static final String DBNAME = "recipe.db";

    /**
     * Version of the database. Increment this number to trigger an upgrade.
     */
    private static final int VERSION = 1;

    /**
     * Constructs a new helper object for creating, opening, and/or managing the database.
     * The database is not actually created or opened until one of {@link #getWritableDatabase()}
     * or {@link #getReadableDatabase()} is called.
     *
     * @param context The context in which the database is being accessed.
     */
    public MyDBOpenHelper(Context context) {
        super(context, DBNAME, null, VERSION);
    }

    /**
     * Called when the database is created for the first time. This is where the creation of
     * tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement for creating a new table for storing recipe information.
        db.execSQL("CREATE TABLE repair(id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(10), region VARCHAR(10), url VARCHAR(20))");
    }

    /**
     * Called when the database needs to be upgraded. The implementation should use this method to
     * drop tables, add tables, or do anything else it needs to upgrade to the new schema version.
     *
     * @param db The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Implement schema changes and data message here when upgrading.
    }
}
