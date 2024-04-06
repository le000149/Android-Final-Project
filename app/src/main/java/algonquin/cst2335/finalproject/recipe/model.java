package algonquin.cst2335.finalproject.recipe;

import java.util.List;

public class model {
    private List<ResultsDTO> results;

    private Integer offset;

    private Integer number;

    private Integer totalResults;


    public static class ResultsDTO {

        private Integer id;

        private String title;

        private String image;

        private String imageType;

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
    }


    public List<ResultsDTO> getResults() {
        return results;
    }

    public void setResults(List<ResultsDTO> results) {
        this.results = results;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }
}
