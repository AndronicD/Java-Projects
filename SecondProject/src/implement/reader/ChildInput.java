package implement.reader;

import enums.Category;
import enums.Cities;

import java.util.List;

public class ChildInput {
    private Integer id;
    private String lastName;
    private String firstName;
    private Integer age;
    private Cities city;
    private Double niceScore;
    private List<Category> giftsPreferences;
    private Integer niceScoreBonus;
    private String elf;

    /**
     * getter
     */
    public Integer getNiceScoreBonus() {
        return niceScoreBonus;
    }

    /**
     * setter
     */
    public void setNiceScoreBonus(final Integer niceScoreBonus) {
        this.niceScoreBonus = niceScoreBonus;
    }

    /**
     * getter
     */
    public String getElf() {
        return elf;
    }

    /**
     * setter
     */
    public void setElf(final String elf) {
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
    public String getLastName() {
        return lastName;
    }

    /**
     * setter
     */
    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    /**
     * getter
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * setter
     */
    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    /**
     * getter
     */
    public Integer getAge() {
        return age;
    }

    /**
     * setter
     */
    public void setAge(final Integer age) {
        this.age = age;
    }

    /**
     * getter
     */
    public Cities getCity() {
        return city;
    }

    /**
     * setter
     */
    public void setCity(final Cities city) {
        this.city = city;
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
