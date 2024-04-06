/*
 * File: DictionaryEntry.java
 * Author: Zhenni Lu
 * Lab Section: 032
 * Creation Date: April 4, 2024
 *
 * Description:
 * This class represents an entry in the dictionary, consisting of a word and its definitions.
 */

package algonquin.cst2335.finalproject.ui;

import java.util.List;

/**
 * Represents an entry in the dictionary, consisting of a word and its definitions.
 */
public class DictionaryEntry {
    private String word;
    private List<String> definitions;

    /**
     * Constructs a new DictionaryEntry object with the specified word and definitions.
     *
     * @param word        The word.
     * @param definitions The list of definitions associated with the word.
     */
    public DictionaryEntry(String word, List<String> definitions) {
        this.word = word;
        this.definitions = definitions;
    }

    /**
     * Gets the word.
     *
     * @return The word.
     */
    public String getWord() {
        return word;
    }

    /**
     * Sets the word.
     *
     * @param word The word to set.
     */
    public void setWord(String word) {
        this.word = word;
    }

    /**
     * Gets the list of definitions associated with the word.
     *
     * @return The list of definitions.
     */
    public List<String> getDefinitions() {
        return definitions;
    }

    /**
     * Sets the list of definitions associated with the word.
     *
     * @param definitions The list of definitions to set.
     */
    public void setDefinitions(List<String> definitions) {
        this.definitions = definitions;
    }
}
