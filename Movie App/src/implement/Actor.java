package implement;

import actor.ActorsAwards;

import java.util.ArrayList;
import java.util.Map;

public class Actor {
    private String name;
    private String description;
    private ArrayList<String> movies;
    private Map<ActorsAwards, Integer> awards;
    public double filmography_rating = 0.0;
    public int number_of_rated_filmography = 0;

    public Actor() {
    }

    /**
     *intoarce filmul cu numele dat ca parametru
     */
    public Movie video_type_movie(String title, ArrayList<Movie> movie_list) {
        for (Movie movie : movie_list) {
            if (movie.getName().equals(title))
                return movie;
        }
        return null;
    }

    /**
     *intoarce serialul cu numele dat ca parametru
     */
    public Series video_type_series(String title, ArrayList<Series> series_list) {
        for (Series series : series_list) {
            if (series.getName().equals(title))
                return series;
        }
        return null;
    }

    /**
     * seteaza nota totala pentru filmografia actorului
     */
    public void get_rating_by_filmography(ArrayList<Movie> movie_list, ArrayList<Series> series_list) {
        if (filmography_rating > 0.0) {
            return;
        }
        for (String t : this.movies) {
            if (this.video_type_movie(t, movie_list) != null) {
                Movie movie = this.video_type_movie(t, movie_list);
                if (movie != null) {
                    if (movie.getfinalrating(movie.getRatings()) > 0.0) {
                        this.filmography_rating += movie.getfinalrating(movie.getRatings());
                        number_of_rated_filmography++;
                    }
                }
            }
            else {
                Series series = this.video_type_series(t, series_list);
                if (series != null) {
                    if (series.getRating() > 0.0) {
                        this.filmography_rating += series.getRating();
                        number_of_rated_filmography++;
                    }
                }
            }
        }
        if (this.filmography_rating > 0.0) {
            this.filmography_rating = this.filmography_rating / number_of_rated_filmography;
        }
        else {
            this.filmography_rating = 0.0;
        }
    }

    /**
     *setter pentru nume actor
     */
    public String getName() {
        return name;
    }

    /**
     *setter pentru nume actor
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *getter pentru descriere actor
     */
    public String getDescription() {
        return description;
    }

    /**
     *setter pentru descriere actor
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *getter pentru lista de filme
     */
    public ArrayList<String> getMovies() {
        return movies;
    }

    /**
     *setter pentru lista de filme
     */
    public void setMovies(ArrayList<String> movies) {
        this.movies = movies;
    }

    /**
     *getter pentru premii actor
     */
    public Map<ActorsAwards, Integer> getAwards() {
        return awards;
    }

    /**
     *setter pentru premii actor
     */
    public void setAwards(Map<ActorsAwards, Integer> awards) {
        this.awards = awards;
    }
}
