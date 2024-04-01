package org.algonquin.cst2355.finalproject.dictionary.model;

/**
 * This is for the CST2355 Final Project - Dictionary Topic
 * Author: Yeqing Xia
 * Lab Section: CST2335 013
 * Creation Date: 2024/03/31
 */

import androidx.room.Entity;
import androidx.room.PrimaryKey;


/**
 * Entity class representing a word definition.
 * This class is used with a database to store and retrieve word definitions.
 */
@Entity
public class Definition {

    /**
     * represents the id of the definition
     */
    @PrimaryKey(autoGenerate = true)
    private int id;

    /**
     * represents the word to be searched
     */
    private String word;

    /**
     * represents the definition
     */
    private String definition;

    /**
     * represents the phonetic
     */
    private String phonetic;
    /**
     * represents the audio of phonetic
     */
    private String audio;

    /**
     * represents the part of speech
     */
    private String partOfSpeech;

    /**
     * This constructor is used to create a new Definition instance.
     *
     * @param word word to be searched
     * @param definition definition of the word
     * @param phonetic phonetic of the word
     * @param partOfSpeech part of speech of the word
     */
    public Definition(String word, String definition, String phonetic, String audio, String partOfSpeech) {
        this.word = word;
        this.definition = definition;
        this.phonetic = phonetic;
        this.audio = audio;
        this.partOfSpeech = partOfSpeech;
    }

    /**
     * Get the id of the definition
     * @return the id of the definition
     */
    public int getId() {
        return id;
    }

    /**
     * Set the id of the definition, usually called by the database to assign an auto generated id
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Get the word of the definition
     * @return the word of the definition
     */
    public String getWord() {
        return word;
    }

    /**
     * Set the word of the definition
     * @param word the word to set for the definition
     */
    public void setWord(String word) {
        this.word = word;
    }

    /**
     * Get the definition
     * @return the definition
     */
    public String getDefinition() {
        return definition;
    }

    /**
     * Set the definition
     * @param definition the definition to set
     */
    public void setDefinition(String definition) {
        this.definition = definition;
    }


    public String getPhonetic() {
        return phonetic;
    }

    public void setPhonetic(String phonetic) {
        this.phonetic = phonetic;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(String partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }
}
