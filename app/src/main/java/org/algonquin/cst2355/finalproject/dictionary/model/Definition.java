package org.algonquin.cst2355.finalproject.dictionary.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Definition {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String word;
    private String definition;

    public Definition(String word, String definition) {
        this.word = word;
        this.definition = definition;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

}
