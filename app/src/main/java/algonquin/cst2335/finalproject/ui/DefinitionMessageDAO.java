/*
 * File: DefinitionMessageDAO.java
 * Author: Zhenni Lu
 * Lab Section: 032
 * Creation Date: April 4, 2024
 *
 * Description:
 * Data Access Object (DAO) interface for interacting with the DefinitionMessage entity in the Room database.
 */

package algonquin.cst2335.finalproject.ui;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * Data Access Object (DAO) interface for interacting with the DefinitionMessage entity in the Room database.
 */
@Dao
public interface DefinitionMessageDAO {

    /**
     * Inserts a new DefinitionMessage into the database.
     *
     * @param d The DefinitionMessage to insert.
     * @return The ID of the inserted message.
     */
    @Insert
    long insertMessage(DefinitionMessage d);

    /**
     * Retrieves all DefinitionMessages from the database.
     *
     * @return A list of all DefinitionMessages stored in the database.
     */
    @Query("SELECT * FROM DefinitionMessage")
    List<DefinitionMessage> getAllMessages();

    /**
     * Deletes a DefinitionMessage from the database based on the provided word and definition.
     *
     * @param word       The word of the DefinitionMessage to delete.
     * @param definition The definition of the DefinitionMessage to delete.
     */
    @Query("DELETE FROM DefinitionMessage WHERE word = :word AND definition = :definition")
    void deleteWordAndDefinition(String word, String definition);
}
