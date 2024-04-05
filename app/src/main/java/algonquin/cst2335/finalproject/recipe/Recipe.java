package algonquin.cst2335.finalproject.recipe;
// Recipe.java
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "recipes")
public class Recipe {
    @PrimaryKey(autoGenerate = true)

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "image")
    public String image;

    @ColumnInfo(name = "summary")
    public String summary;

    @ColumnInfo(name = "source_url")
    public String sourceUrl;

    // Constructor
    public Recipe(String title, String image, String summary, String sourceUrl) {
        this.title = title;
        this.image = image;
        this.summary = summary;
        this.sourceUrl = sourceUrl;
    }
    private int id;
    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }
}


