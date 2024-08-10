package implement;


import java.util.ArrayList;

public class Video {
    private String name;
    private Integer year;
    private ArrayList<String> genres;

    /**
     *getter pentru nume video
     */
    public String getName() {
        return name;
    }

    /**
     *setter pentru nume video
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *getter pentru an
     */
    public Integer getYear() {
        return year;
    }

    /**
     *setter pentru an
     */
    public void setYear(Integer year) {
        this.year = year;
    }

    /**
     *getter pentru lista genuri
     */
    public ArrayList<String> getGenres() {
        return genres;
    }

    /**
     *setter pentru lista genuri
     */
    public void setGenres(ArrayList<String> genres) {
        this.genres = genres;
    }
}