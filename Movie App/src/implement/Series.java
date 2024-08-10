package implement;

import entertainment.Season;
import java.util.ArrayList;


public class Series extends Video {
    private ArrayList<String> cast;
    private ArrayList<Season> seasons = new ArrayList<>();
    private Double rating = 0.0;

    /**
     *constructor pentru serial
     */
    public Series() {
    }

    /**
     *intoarce timpul total pe toate sezoanele
     */
    public Integer getDuration() {
        int final_duration = 0;
        for (Season s : this.seasons) {
            final_duration += s.getDuration();
        }
        return final_duration/this.seasons.size();
    }

    /**
     *getter pentru lista de actori
     */
    public ArrayList<String> getCast() {
        return cast;
    }

    /**
     *setter pentru lista de actori
     */
    public void setCast(ArrayList<String> cast) {
        this.cast = cast;
    }

    /**
     *getter pentru lista sezoane
     */
    public ArrayList<Season> getSeasons() {
        return seasons;
    }

    /**
     *setter pentru lista sezoane
     */
    public void setSeasons(ArrayList<Season> seasons) {
        this.seasons = seasons;
    }

    /**
     *calculeaza rating-ul final si seteaza campul rating
     */
    public void CalcRating() {
        double rating1 = 0.0;
        for (Season sis : this.seasons) {
            if (sis.getRatings().get(0) != 0.0) {
                for (double d_number : sis.getRatings()) {
                    rating1 += d_number / sis.getRatings().size();
                }
                this.rating += rating1;
                rating1 = 0.0;
            }
        }
        if (this.rating > 0.0) {
            this.rating /= seasons.size();
        }
    }

    /**
     *getter pentru rating
     */
    public Double getRating(){
        return rating;
    }

    /**
     *setter pentru rating
     */
    public void setRating(Double rating) {
        this.rating = rating;
    }
}
