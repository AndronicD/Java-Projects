package implement;

import java.util.ArrayList;

public class Movie extends Video {
    private Integer duration;
    private ArrayList<Double> ratings = new ArrayList<>();
    private ArrayList<String> actors;

    /**
     *constructor obiect film
     */
    public Movie() {
    }

    /**
     *getter pentru durata
     */
    public Integer getDuration() {
        return duration;
    }

    /**
     *setter pentru durata
     */
    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    /**
     *getter pentru lista de rating-uri
     */
    public ArrayList<Double> getRatings() {
        return ratings;
    }

    /**
     *setter pentru lista ratinguri
     */
    public void setRatings(ArrayList<Double> ratings) {
        this.ratings = ratings;
    }

    /**
     *getter pentru lista nume actori
     */
    public ArrayList<String> getActors() {
        return actors;
    }

    /**
     *setter pentru lista nume actori
     */
    public void setActors(ArrayList<String> actors) {
        this.actors = actors;
    }

    /**
     *intoarce reting-ul filmului calculat in urma rating-urilor
     */
    public double getfinalrating(ArrayList<Double> ratings) {
        double sum = 0.0;
        if (ratings.get(0) != 0.0) {
            for (Double i : ratings) {
                sum += i;
            }
            sum /= ratings.size();
        }
        return sum;
    }
}
