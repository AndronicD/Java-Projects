package implement.action.query;

import common.Constants;
import implement.Movie;
import implement.User;

import java.util.*;

public class Movie_Query extends Query{
    public StringBuilder message = new StringBuilder();

    /**
     *constructor query filme
     */
    public Movie_Query() {}

    /**
     *intoarce mesajul pentru output
     */
    public StringBuilder get_message(ArrayList<Movie> movies) {
        this.message.append("Query result: ");
        this.message.append("[");
        for(int i = 0; i < movies.size(); i++) {
            if(i < movies.size()-1){
                this.message.append(movies.get(i).getName());
                this.message.append(", ");
            }
            else{
                this.message.append(movies.get(i).getName());
            }
        }
        this.message.append("]");
        return this.message;
    }

    /**
     *sorteaza map in functie de valoarea cheii aceasta fiind double
     */
    public static HashMap<String, Double> sortByValue_double(HashMap<String, Double> hm) {
        List<Map.Entry<String, Double> > list =
                new LinkedList<>(hm.entrySet());
        list.sort(Map.Entry.comparingByValue());
        HashMap<String, Double> temp = new LinkedHashMap<>();
        for (Map.Entry<String, Double> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    /**
     *sorteaza map in functie de valoarea cheii aceasta fiind integer
     */
    public static HashMap<String, Integer> sortByValue(HashMap<String, Integer> hm) {
        List<Map.Entry<String, Integer> > list =
                new LinkedList<>(hm.entrySet());
        list.sort(Map.Entry.comparingByValue());
        HashMap<String, Integer> temp = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    /**
     *sorteaza in functie de primul caracter
     */
    static class Sortbynameofratingsasc implements Comparator<Movie> {
        public int compare(Movie a, Movie b)
        {
            return (a.getName().charAt(0) - b.getName().charAt(0));
        }
    }

    /**
     *sorteaza in functie de tipul de sortare
     */
    public ArrayList<Movie> sort(String sorttype, ArrayList<Movie> list) {
        list.sort(new Sortbynameofratingsasc());
        if (sorttype.equals(Constants.DESC)) {
            Collections.reverse(list);
            return list;
        }
        return list;
    }

    /**
     *sorteaza in functie de numarul de elemente
     */
    public ArrayList<Movie> number(int number, ArrayList<Movie> list) {
        if (number < list.size()) {
            ArrayList<Movie> temp = new ArrayList<>();
            for (int i = 0; i < number; i++) {
                temp.add(list.get(i));
            }
            list = temp;
        }
        return list;
    }

    /**
     *intoarce obiect de tip movie daca se gaseste numele in lista
     */
    public Movie find_movie_by_title(ArrayList<Movie> list, String title) {
        for (Movie movie : list) {
            if (movie.getName().equals(title)) {
                return movie;
            }
        }
        return null;
    }

    /**
     *elimina element din lista in functie de an
     */
    public ArrayList<Movie> search_by_year(ArrayList<Movie> list, String year) {
        Integer i = Integer.parseInt(year);
        list.removeIf(movie -> !movie.getYear().equals(i));
        return list;
    }

    /**
     *elimina element din lista in functie de gen
     */
    public ArrayList<Movie> search_by_genre(ArrayList<Movie> list, String genre) {
        list.removeIf(movie -> !movie.getGenres().contains(genre));
        return list;
    }

    /**
     *intoarec rezultat query
     */
    public ArrayList<Movie> get_movie_list_by_filters(List<List<String>> filters, ArrayList<Movie> movies,
                                                      String criteria, int number, String sorttype,
                                                      ArrayList<User> users){
        ArrayList<Movie> new_movie_list = new ArrayList<>(movies);
        switch (criteria) {
            case Constants.FAVORITE -> {
                ArrayList<String> movies_in_favorite = new ArrayList<>();
                //adaug toate filmele din listele de favorite
                for (User user : users) {
                    for (Movie movie : movies) {
                        if (user.getFavourite().contains(movie.getName())) {
                            movies_in_favorite.add(movie.getName());
                        }
                    }
                }
                //elimin elemente in functie de filtre
                for (String t : filters.get(0)) {
                    if (t != null) {
                        new_movie_list = this.search_by_year(movies, t);
                        new_movie_list = this.sort(sorttype, new_movie_list);
                        new_movie_list = this.number(number, new_movie_list);
                    }
                }
                for (String t : filters.get(1)) {
                    if (t != null) {
                        new_movie_list = this.search_by_genre(movies, t);
                        new_movie_list = this.sort(sorttype, new_movie_list);
                        new_movie_list = this.number(number, new_movie_list);
                    }
                }
                //adaug intr-o noua lista intersectia dintre lista de filme si lista de filme favorite
                ArrayList<Movie> temporary = new ArrayList<>();
                for (Movie movie : new_movie_list) {
                    for (String s : movies_in_favorite) {
                        if (movie.getName().equals(s)) {
                            temporary.add(movie);
                        }
                    }
                }
                new_movie_list = temporary;
            }
            case Constants.RATINGS -> {
                HashMap<String, Double> movie_rating = new HashMap<>();
                for (Movie movie : movies) {
                    movie_rating.put(movie.getName(), movie.getfinalrating(movie.getRatings()));
                }
                //sortez map-ul de filme si rating-uri
                HashMap<String, Double> movie_rating1 = sortByValue_double(movie_rating);
                ArrayList<Movie> temp1 = new ArrayList<>();
                //elimin daca are rating 0
                for (Map.Entry<String, Double> en : movie_rating1.entrySet()) {
                    if (en.getValue() > 0) {
                        Movie movie = this.find_movie_by_title(movies, en.getKey());
                        temp1.add(movie);
                    }
                }
                new_movie_list = temp1;
                //filtez filmele in functie de criterii
                for (String t : filters.get(0)) {
                    if (t != null) {
                        new_movie_list = this.search_by_year(new_movie_list, t);
                        new_movie_list = this.sort(sorttype, new_movie_list);
                        new_movie_list = this.number(number, new_movie_list);
                    }
                }
                for (String t : filters.get(1)) {
                    if (t != null) {
                        new_movie_list = this.search_by_genre(new_movie_list, t);
                        new_movie_list = this.sort(sorttype, new_movie_list);
                        new_movie_list = this.number(number, new_movie_list);
                    }
                }
            }
            case Constants.LONGEST -> {
                HashMap<String, Integer> movie_duration = new HashMap<>();
                for (Movie movie : movies) {
                    movie_duration.put(movie.getName(), movie.getDuration());
                }
                //sortez in functie de durata filmului
                HashMap<String, Integer> movie_duration1 = sortByValue(movie_duration);
                ArrayList<Movie> temp2 = new ArrayList<>();
                for (Map.Entry<String, Integer> en : movie_duration1.entrySet()) {
                    if (en.getValue() > 0) {
                        Movie movie = this.find_movie_by_title(movies, en.getKey());
                        temp2.add(movie);
                    }
                }
                new_movie_list = temp2;
                //filtrez in functie de criterii
                for (String t : filters.get(0)) {
                    if (t != null) {
                        new_movie_list = this.search_by_year(new_movie_list, t);
                        new_movie_list = this.sort(sorttype, new_movie_list);
                        new_movie_list = this.number(number, new_movie_list);
                    }
                }
                for (String t : filters.get(1)) {
                    if (t != null) {
                        new_movie_list = this.search_by_genre(new_movie_list, t);
                        new_movie_list = this.sort(sorttype, new_movie_list);
                        new_movie_list = this.number(number, new_movie_list);
                    }
                }
            }
            case Constants.MOST_VIEWED -> {
                HashMap<String, Integer> movie_total_views = new HashMap<>();
                for (Movie movie : movies) {
                    movie_total_views.put(movie.getName(), 0);
                }
                //calculez numarul total de vizualizari per film
                for (User user : users) {
                    for (Map.Entry<String, Integer> each : user.getHistory().entrySet()) {
                        for (Map.Entry<String, Integer> mov : movie_total_views.entrySet()) {
                            if (each.getKey().equals(mov.getKey())) {
                                mov.setValue(mov.getValue() + each.getValue());
                            }
                        }
                    }
                }
                //sortez descrescator in functie de valoare
                HashMap<String, Integer> movie_total_views1 = sortByValue(movie_total_views);
                ArrayList<Movie> temp3 = new ArrayList<>();
                for (Map.Entry<String, Integer> en : movie_total_views1.entrySet()) {
                    if (en.getValue() > 0) {
                        Movie movie = this.find_movie_by_title(movies, en.getKey());
                        temp3.add(movie);
                    }
                }
                new_movie_list = temp3;
                //elimin in functie de filtre
                for (String t : filters.get(0)) {
                    if (t != null) {
                        new_movie_list = this.search_by_year(new_movie_list, t);
                        new_movie_list = this.sort(sorttype, new_movie_list);
                        new_movie_list = this.number(number, new_movie_list);
                    }
                }
                for (String t : filters.get(1)) {
                    if (t != null) {
                        new_movie_list = this.search_by_genre(new_movie_list, t);
                        new_movie_list = this.sort(sorttype, new_movie_list);
                        new_movie_list = this.number(number, new_movie_list);
                    }
                }
            }
        }
        return new_movie_list;
    }
}
