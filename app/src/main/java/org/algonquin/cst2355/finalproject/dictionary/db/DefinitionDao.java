package org.algonquin.cst2355.finalproject.dictionary.db;
/**
 * This is for the CST2355 Final Project - Dictionary Topic
 * Author: Yeqing Xia
 * Lab Section: CST2335 013
 * Creation Date: 2024/03/31
 */

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import org.algonquin.cst2355.finalproject.dictionary.model.Definition;

import java.util.List;

/**
 * Data Access Object (DAO) interface for the Definition entity
 * Provides methods for interacting with the Definition table in the database
 */
@Dao
public interface DefinitionDao {

    /**
     *Retrieve list of words definitions sorted by id
     */
    @Query("SELECT * FROM Definition WHERE id IN (SELECT MIN(id) FROM Definition GROUP BY word) ORDER BY id DESC")
    List<Definition> getAllDefinitionDistinct();

    /**
     * Retrieve a list of definitions for a given word from the database
     * @param word the word for which definitions are being queried
     */
    @Query("select * from Definition where word = :word")
    List<Definition> getDefinitions(String word);

    /**
     * Insert a list of definitions into the database
     * if the definition already exists it will be ignored
     * @param definitions the list of definitions to be inserted
     */
    @Insert
    void saveDefinitions(List<Definition> definitions);

    /**
     * Delete all definitions for a given word
     * @param word the word for which definitions are to be deleted
     */
    @Query("delete from Definition where word = :word")
    void deleteDefinition(String word);

}