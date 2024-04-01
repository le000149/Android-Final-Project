package org.algonquin.cst2355.finalproject.dictionary.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import org.algonquin.cst2355.finalproject.dictionary.model.Definition;

@Database(entities = {Definition.class}, version = 1)
public abstract class DictionaryDatabase extends RoomDatabase {
    public static final String NAME = "DictionaryDatabase";

    public abstract DefinitionDao DefinitionDao();
}