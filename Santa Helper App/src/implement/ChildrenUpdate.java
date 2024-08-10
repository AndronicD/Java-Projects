package implement;

import enums.Category;


import java.util.List;

public class ChildrenUpdate {
    private Integer id;
    private Double niceScore;
    private List<Category> giftsPreferences;
    private String elf;

    /**
     * getter
     */
    public String getElf() {
        return elf;
    }

    /**
     * setter
     */
    public void setElf(final  String elf) {
        this.elf = elf;
    }

    /**
     * getter
     */
    public Integer getId() {
        return id;
    }

    /**
     * setter
     */
    public void setId(final Integer id) {
        this.id = id;
    }

    /**
     * getter
     */
    public Double getNiceScore() {
        return niceScore;
    }

    /**
     * setter
     */
    public void setNiceScore(final Double niceScore) {
        this.niceScore = niceScore;
    }

    /**
     * getter
     */
    public List<Category> getGiftsPreferences() {
        return giftsPreferences;
    }

    /**
     * setter
     */
    public void setGiftsPreferences(final List<Category> giftsPreferences) {
        this.giftsPreferences = giftsPreferences;
    }
}
