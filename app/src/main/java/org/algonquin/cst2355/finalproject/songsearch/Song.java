package org.algonquin.cst2355.finalproject.songsearch;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Song {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    private int id;

    private String word;

    private String Song;

    public Song(String word, String Song) {
        this.word = word;
        this.Song = Song;
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

    public String getSong() {
        return Song;
    }

    public void setSong(String Song) {
        this.Song = Song;
    }
}
