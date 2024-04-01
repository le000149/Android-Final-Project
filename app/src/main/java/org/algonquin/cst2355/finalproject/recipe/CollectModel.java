package org.algonquin.cst2355.finalproject.recipe;
/**
 * This class represents a model for a collected recipe.
 */
public class CollectModel {
    String name;
    String region;
    String url;
    /**
     * Get the name of the collected recipe.
     *
     * @return The name of the collected recipe.
     */
    public String getName() {
        return name;
    }
    /**
     * Set the name of the collected recipe.
     *
     * @param name The name of the collected recipe.
     */
    public void setName(String name) {
        this.name = name;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getUrl() {
        return url;
    }
    /**
     * Set the URL of the collected recipe.
     *
     * @param url The URL of the collected recipe.
     */
    public void setUrl(String url) {
        this.url = url;
    }
}
