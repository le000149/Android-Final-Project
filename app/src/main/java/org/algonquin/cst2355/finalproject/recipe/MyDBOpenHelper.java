package org.algonquin.cst2355.finalproject.recipe;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * Helper class for managing database creation and version management.
 */

public class MyDBOpenHelper extends SQLiteOpenHelper {

    private static final String DBNAME="recipe.db";
    private static final int VERSION=1;
    /**
     * Constructor for the MyDBOpenHelper class.
     *
     * @param context The context in which the database will be used.
     */
    public MyDBOpenHelper(Context context) {
        super(context, DBNAME, null, VERSION);
    }
    /**
     * Called when the database is created for the first time.
     *
     * @param db The database instance.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table repair(id INTEGER primary key autoincrement,name varchar(10),region varchar(10),url varchar(20))");
    }
    //update database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }
}