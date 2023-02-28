package implement;

import java.util.ArrayList;
import java.util.Map;

public class User {
    private String name;
    private String subscription;
    private Map<String, Integer> history;
    private ArrayList<String> favourite;
    private int number_of_ratings;

    /**
     *getter pentru numar de rating-uri
     */
    public int getNumber_of_ratings() {
        return number_of_ratings;
    }

    /**
     *setter pentru numar de rating-uri
     */
    public void setNumber_of_ratings(int number_of_ratings) {
        this.number_of_ratings = number_of_ratings;
    }

    /**
     *constructor pentru user
     */
    public User() {
    }

    /**
     *getter pentru tipul de abonament
     */
    public String getSubscription() {
        return subscription;
    }

    /**
     *setter pentru tipul de abonament
     */
    public void setSubscription(String subscription) {
        this.subscription = subscription;
    }

    /**
     *getter pentru nume utilizator
     */
    public String getName() {
        return name;
    }

    /**
     *setter pentru nume utilizator
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *getter pentru istoric vizionari
     */
    public Map<String, Integer> getHistory() {
        return history;
    }

    /**
     *setter pentru istoric vizionari
     */
    public void setHistory(Map<String, Integer> history) {
        this.history = history;
    }

    /**
     *getter pentru lista video-uri favorite
     */
    public ArrayList<String> getFavourite() {
        return favourite;
    }

    /**
     *setter pentru lista video-uri favorite
     */
    public void setFavourite(ArrayList<String> favourite) {
        this.favourite = favourite;
    }
}
