package algonquin.cst2335.finalproject.recipe;

/**
 * Represents a model for storing information about a collected recipe.
 * This class holds details about a recipe, including its name, the region it comes from, and a URL for more information.
 */
public class CollectModel {
    // Fields to store the recipe's name, region, and URL.
    private String name;
    private String region;
    private String url;

    /**
     * Gets the name of the recipe.
     *
     * @return The name of the recipe.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the recipe.
     *
     * @param name The name to set for the recipe.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the region associated with the recipe.
     *
     * @return The region of the recipe.
     */
    public String getRegion() {
        return region;
    }

    /**
     * Sets the region associated with the recipe.
     *
     * @param region The region to set for the recipe.
     */
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * Gets the URL for the recipe.
     *
     * @return The URL of the recipe.
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the URL of the recipe.
     *
     * @param url The URL to set for the recipe.
     */
    public void setUrl(String url) {
        this.url = url;
    }
}

