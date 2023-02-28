package implement;

import enums.Category;
import enums.Cities;

import java.util.ArrayList;
import java.util.List;

public class Child {
    private Integer id;
    private String lastName;
    private String firstName;
    private Cities city;
    private Integer age;
    private List<Category> giftsPreferences;
    private Double averageScore;
    private List<Double> niceScoreHistory;
    private Double assignedBudget;
    private List<Gift> receivedGifts;

    public Child() {
        this.niceScoreHistory = new ArrayList<>();
        this.receivedGifts = new ArrayList<>();
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
    public List<Category> getGiftsPreferences() {
        return giftsPreferences;
    }

    /**
     * setter
     */
    public void setGiftsPreferences(final List<Category> giftsPreferences) {
        this.giftsPreferences = giftsPreferences;
    }

    /**
     * getter
     */
    public Double getAverageScore() {
        return averageScore;
    }

    /**
     * setter
     */
    public void setAverageScore(final Double averageScore) {
        this.averageScore = averageScore;
    }

    /**
     * getter
     */
    public List<Double> getNiceScoreHistory() {
        return niceScoreHistory;
    }

    /**
     * setter
     */
    public void setNiceScoreHistory(final List<Double> niceScoreHistory) {
        this.niceScoreHistory = niceScoreHistory;
    }

    /**
     * getter
     */
    public Double getAssignedBudget() {
        return assignedBudget;
    }

    /**
     * setter
     */
    public void setAssignedBudget(final Double assignedBudget) {
        this.assignedBudget = assignedBudget;
    }

    /**
     * getter
     */
    public List<Gift> getReceivedGifts() {
        return receivedGifts;
    }

    /**
     * setter
     */
    public void setReceivedGifts(final List<Gift> receivedGifts) {
        this.receivedGifts = receivedGifts;
    }
}
