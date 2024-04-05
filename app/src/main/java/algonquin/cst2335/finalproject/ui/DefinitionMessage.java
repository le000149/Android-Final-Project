package algonquin.cst2335.finalproject.ui;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DefinitionMessage {

    @ColumnInfo(name = "word")
    protected String word;

    @ColumnInfo(name = "definition")
    protected String definition;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    protected long id;

    public DefinitionMessage(String word, String definition) {
        this.word = word;
        this.definition = definition;
    }

    public void setId(long id) {
        this.id = id;
    }


    public long getId(){return id;}
    public String getWord() {
        return word;
    }

    public String getDefinition() {
        return definition;
    }
}
