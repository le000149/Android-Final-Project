package org.algonquin.cst2355.finalproject.recipe;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBOpenHelper extends SQLiteOpenHelper {
    //定义数据库名和版本号
    private static final String DBNAME="recipe.db";
    private static final int VERSION=1;
    public MyDBOpenHelper(Context context) {
        super(context, DBNAME, null, VERSION);
    }
    //create database
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