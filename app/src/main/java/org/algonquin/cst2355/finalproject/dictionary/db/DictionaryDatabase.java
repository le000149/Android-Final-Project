package org.algonquin.cst2355.finalproject.dictionary.db;

/**
 * This is for the CST2355 Final Project - Dictionary Topic
 * Author: Yeqing Xia
 * Lab Section: CST2335 013
 * Creation Date: 2024/03/31
 */

import androidx.room.Database;
import androidx.room.RoomDatabase;

import org.algonquin.cst2355.finalproject.dictionary.model.Definition;


/**
 * abstract class for dictionary databases
 */
@Database(entities = {Definition.class}, version = 3)
public abstract class DictionaryDatabase extends RoomDatabase {

    /**
     * name of the database
     */
    public static final String NAME = "DictionaryDatabase";

    /**
     * get the definition dao
     * @return definition dao
     */
    public abstract DefinitionDao DefinitionDao();// automatic implementation
}