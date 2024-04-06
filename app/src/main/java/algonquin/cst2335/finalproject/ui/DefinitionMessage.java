/*
 * File: DefinitionMessage.java
 * Author: Zhenni Lu
 * Lab Section: 032
 * Creation Date: April 4, 2024
 *
 * Description:
 * This class represents a definition message entity, which is used in the Room database.
 */

package algonquin.cst2335.finalproject.ui;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Represents a definition message entity used in the Room database.
 */
@Entity
public class DefinitionMessage {

    @ColumnInfo(name = "word")
    protected String word;

    @ColumnInfo(name = "definition")
    protected String definition;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    protected long id;

    /**
     * Constructs a new DefinitionMessage object with the specified word and definition.
     *
     * @param word       The word for which the definition is provided.
     * @param definition The definition of the word.
     */
    public DefinitionMessage(String word, String definition) {
        this.word = word;
        this.definition = definition;
    }

    /**
     * Sets the ID of the DefinitionMessage.
     *
     * @param id The ID to be set.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Gets the ID of the DefinitionMessage.
     *
     * @return The ID of the DefinitionMessage.
     */
    public long getId() {
        return id;
    }

    /**
     * Gets the word associated with the DefinitionMessage.
     *
     * @return The word associated with the DefinitionMessage.
     */
    public String getWord() {
        return word;
    }

    /**
     * Gets the definition associated with the DefinitionMessage.
     *
     * @return The definition associated with the DefinitionMessage.
     */
    public String getDefinition() {
        return definition;
    }
}
