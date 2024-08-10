package implement.action;

import java.util.List;

public class Action {
    private Integer id;
    private String action_type;
    private String type;
    private String username;
    private String objectType;
    private String sortType;
    private String criteria;
    private String title;
    private int number;
    private int seasonNumber;
    private double grade;
    private String genre;
    private List<List<String>> filters;

    /**
    *getter pentru genul de video
     */
    public String getGenre() {
        return genre;
    }


    /**
     *setter pentru genul de video
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }

    /**
     *getter pentru nota
     */
    public double getGrade() {
        return grade;
    }

    /**
     *setter pentru nota
     */
    public void setGrade(double grade) {
        this.grade = grade;
    }

    /**
     *getter pentru lista de filtre
     */
    public List<List<String>> getFilters() {
        return filters;
    }

    /**
     *setter pentru lista de filtre
     */
    public void setFilters(List<List<String>> filters) {
        this.filters = filters;
    }

    /**
     *getter pentru numarul sezonului
     */
    public int getSeasonNumber() {
        return seasonNumber;
    }

    /**
     *setter pentru numarul sezonului
     */
    public void setSeasonNumber(int seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    /**
     *getter pentru numar
     */
    public int getNumber() {
        return number;
    }

    /**
     *setter pentru numar
     */
    public void setNumber(int number) {
        this.number = number;
    }

    /**
     *getter pentru titlu
     */
    public String getTitle() {
        return title;
    }

    /**
     *setter pentru titlu
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *getter pentru criteriu
     */
    public String getCriteria() {
        return criteria;
    }

    /**
     *setter pentru criteriu
     */
    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }

    /**
     *getter pentru tipul de sortare
     */
    public String getSortType() {
        return sortType;
    }

    /**
     *setter pentru tipul de sortare
     */
    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    /**
     *getter pentru tipul de obiect
     */
    public String getObjectType() {
        return objectType;
    }

    /**
     *setter pentru tipul de obiect
     */
    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    /**
     *getter pentru nume utilizator
     */
    public String getUsername() {
        return username;
    }

    /**
     *setter pentru nume de utilizator
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     *getter pentru tip
     */
    public String getType() {
        return type;
    }

    /**
     *setter pentru tip
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     *getter pentru id
     */
    public Integer getId() {
        return id;
    }

    /**
     *setter pentru id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     *getter pentru tipul de actiune
     */
    public String getAction_type() {
        return action_type;
    }

    /**
     *setter pentru tipul de actiune
     */
    public void setAction_type(String action_type) {
        this.action_type = action_type;
    }
}
