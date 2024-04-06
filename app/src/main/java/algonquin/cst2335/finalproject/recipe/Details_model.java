package algonquin.cst2335.finalproject.recipe;

import java.util.List;

public class Details_model {
    private Boolean vegetarian;

    private Boolean vegan;

    private Boolean glutenFree;

    private Boolean dairyFree;

    private Boolean veryHealthy;

    private Boolean cheap;

    private Boolean veryPopular;

    private Boolean sustainable;

    private Boolean lowFodmap;

    private Integer weightWatcherSmartPoints;

    private String gaps;

    private Integer preparationMinutes;

    private Integer cookingMinutes;

    private Integer aggregateLikes;

    private Integer healthScore;

    private String creditsText;

    private String license;

    private String sourceName;

    private Double pricePerServing;

    private List<ExtendedIngredientsDTO> extendedIngredients;

    private Integer id;

    private String title;

    private Integer readyInMinutes;

    private Integer servings;

    private String sourceUrl;

    private String image;

    private String imageType;

    private TasteDTO taste;

    private String summary;

    private List<?> cuisines;

    private List<String> dishTypes;

    private List<?> diets;

    private List<?> occasions;

    private WinePairingDTO winePairing;

    private String instructions;

    private List<AnalyzedInstructionsDTO> analyzedInstructions;

    private Object originalId;

    private Double spoonacularScore;

    private String spoonacularSourceUrl;


    public static class TasteDTO {

        private Double sweetness;

        private Double saltiness;

        private Double sourness;

        private Double bitterness;

        private Double savoriness;

        private Double fattiness;

        private Double spiciness;
    }


    public static class WinePairingDTO {

        private List<?> pairedWines;

        private String pairingText;

        private List<?> productMatches;
    }


    public static class ExtendedIngredientsDTO {

        private Integer id;

        private String aisle;

        private String image;

        private String consistency;

        private String name;

        private String nameClean;

        private String original;

        private String originalName;

        private Double amount;

        private String unit;

        private List<String> meta;

        private ExtendedIngredientsDTO.MeasuresDTO measures;


        public static class MeasuresDTO {

            private ExtendedIngredientsDTO.MeasuresDTO.UsDTO us;

            private ExtendedIngredientsDTO.MeasuresDTO.MetricDTO metric;


            public static class UsDTO {

                private Double amount;

                private String unitShort;

                private String unitLong;
            }


            public static class MetricDTO {

                private Double amount;

                private String unitShort;

                private String unitLong;
            }
        }
    }


    public static class AnalyzedInstructionsDTO {

        private String name;

        private List<StepsDTO> steps;


        public static class StepsDTO {

            private Integer number;

            private String step;

            private List<IngredientsDTO> ingredients;

            private List<EquipmentDTO> equipment;


            public static class IngredientsDTO {

                private Integer id;

                private String name;

                private String localizedName;

                private String image;
            }


            public static class EquipmentDTO {

                private Integer id;

                private String name;

                private String localizedName;
                private String image;
            }
        }
    }

    public Boolean getVegetarian() {
        return vegetarian;
    }

    public void setVegetarian(Boolean vegetarian) {
        this.vegetarian = vegetarian;
    }

    public Boolean getVegan() {
        return vegan;
    }

    public void setVegan(Boolean vegan) {
        this.vegan = vegan;
    }

    public Boolean getGlutenFree() {
        return glutenFree;
    }

    public void setGlutenFree(Boolean glutenFree) {
        this.glutenFree = glutenFree;
    }

    public Boolean getDairyFree() {
        return dairyFree;
    }

    public void setDairyFree(Boolean dairyFree) {
        this.dairyFree = dairyFree;
    }

    public Boolean getVeryHealthy() {
        return veryHealthy;
    }

    public void setVeryHealthy(Boolean veryHealthy) {
        this.veryHealthy = veryHealthy;
    }

    public Boolean getCheap() {
        return cheap;
    }

    public void setCheap(Boolean cheap) {
        this.cheap = cheap;
    }

    public Boolean getVeryPopular() {
        return veryPopular;
    }

    public void setVeryPopular(Boolean veryPopular) {
        this.veryPopular = veryPopular;
    }

    public Boolean getSustainable() {
        return sustainable;
    }

    public void setSustainable(Boolean sustainable) {
        this.sustainable = sustainable;
    }

    public Boolean getLowFodmap() {
        return lowFodmap;
    }

    public void setLowFodmap(Boolean lowFodmap) {
        this.lowFodmap = lowFodmap;
    }

    public Integer getWeightWatcherSmartPoints() {
        return weightWatcherSmartPoints;
    }

    public void setWeightWatcherSmartPoints(Integer weightWatcherSmartPoints) {
        this.weightWatcherSmartPoints = weightWatcherSmartPoints;
    }

    public String getGaps() {
        return gaps;
    }

    public void setGaps(String gaps) {
        this.gaps = gaps;
    }

    public Integer getPreparationMinutes() {
        return preparationMinutes;
    }

    public void setPreparationMinutes(Integer preparationMinutes) {
        this.preparationMinutes = preparationMinutes;
    }

    public Integer getCookingMinutes() {
        return cookingMinutes;
    }

    public void setCookingMinutes(Integer cookingMinutes) {
        this.cookingMinutes = cookingMinutes;
    }

    public Integer getAggregateLikes() {
        return aggregateLikes;
    }

    public void setAggregateLikes(Integer aggregateLikes) {
        this.aggregateLikes = aggregateLikes;
    }

    public Integer getHealthScore() {
        return healthScore;
    }

    public void setHealthScore(Integer healthScore) {
        this.healthScore = healthScore;
    }

    public String getCreditsText() {
        return creditsText;
    }

    public void setCreditsText(String creditsText) {
        this.creditsText = creditsText;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public Double getPricePerServing() {
        return pricePerServing;
    }

    public void setPricePerServing(Double pricePerServing) {
        this.pricePerServing = pricePerServing;
    }

    public List<ExtendedIngredientsDTO> getExtendedIngredients() {
        return extendedIngredients;
    }

    public void setExtendedIngredients(List<ExtendedIngredientsDTO> extendedIngredients) {
        this.extendedIngredients = extendedIngredients;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getReadyInMinutes() {
        return readyInMinutes;
    }

    public void setReadyInMinutes(Integer readyInMinutes) {
        this.readyInMinutes = readyInMinutes;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public TasteDTO getTaste() {
        return taste;
    }

    public void setTaste(TasteDTO taste) {
        this.taste = taste;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<?> getCuisines() {
        return cuisines;
    }

    public void setCuisines(List<?> cuisines) {
        this.cuisines = cuisines;
    }

    public List<String> getDishTypes() {
        return dishTypes;
    }

    public void setDishTypes(List<String> dishTypes) {
        this.dishTypes = dishTypes;
    }

    public List<?> getDiets() {
        return diets;
    }

    public void setDiets(List<?> diets) {
        this.diets = diets;
    }

    public List<?> getOccasions() {
        return occasions;
    }

    public void setOccasions(List<?> occasions) {
        this.occasions = occasions;
    }

    public WinePairingDTO getWinePairing() {
        return winePairing;
    }

    public void setWinePairing(WinePairingDTO winePairing) {
        this.winePairing = winePairing;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public List<AnalyzedInstructionsDTO> getAnalyzedInstructions() {
        return analyzedInstructions;
    }

    public void setAnalyzedInstructions(List<AnalyzedInstructionsDTO> analyzedInstructions) {
        this.analyzedInstructions = analyzedInstructions;
    }

    public Object getOriginalId() {
        return originalId;
    }

    public void setOriginalId(Object originalId) {
        this.originalId = originalId;
    }

    public Double getSpoonacularScore() {
        return spoonacularScore;
    }

    public void setSpoonacularScore(Double spoonacularScore) {
        this.spoonacularScore = spoonacularScore;
    }

    public String getSpoonacularSourceUrl() {
        return spoonacularSourceUrl;
    }

    public void setSpoonacularSourceUrl(String spoonacularSourceUrl) {
        this.spoonacularSourceUrl = spoonacularSourceUrl;
    }
}
