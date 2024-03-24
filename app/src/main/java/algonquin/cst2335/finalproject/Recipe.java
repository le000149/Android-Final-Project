package algonquin.cst2335.finalproject;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "recipes")
public class Recipe {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String imageUrl;
    private String summary;
    private String sourceUrl;

    // Constructors
    public Recipe(String title, String imageUrl, String summary, String sourceUrl) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.summary = summary;
        this.sourceUrl = sourceUrl;
    }
    public int getId() {
        return id;
    }

    // Setter method for id
    public void setId(int id) {
        this.id = id;
    }

    // Getters and setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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
