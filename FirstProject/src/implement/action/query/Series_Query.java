package implement.action.query;

import common.Constants;
import implement.Series;
import implement.User;
import java.util.*;

public class Series_Query extends Query{
    public StringBuilder message = new StringBuilder();

    /**
     *constructor query seriale
     */
    public Series_Query(){
    }

    /**
     *intoarce mesajul pentru output
     */
    public StringBuilder get_message(ArrayList<Series> series){
        this.message.append("Query result: ");
        this.message.append("[");
        for(int i = 0; i < series.size(); i++) {
            if(i < series.size()-1){
                this.message.append(series.get(i).getName());
                this.message.append(", ");
            }
            else{
                this.message.append(series.get(i).getName());
            }
        }
        this.message.append("]");
        return this.message;
    }

    /**
     *gaseste titlul serialului in lista
     */
    public Series find_serie_by_title(ArrayList<Series> list, String title){
        for(Series serie : list){
            if(serie.getName().equals(title)){
                return serie;
            }
        }
        return null;
    }

    /**
     *sorteaza map in functie de valoarea cheii valoarea fiind de tip integer
     */
    public static HashMap<String, Integer> sortByValue(HashMap<String, Integer> hm)
    {
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
     *sorteaza map in functie de valoarea cheii valoarea fiind de tip double
     */
    public static HashMap<String, Double> sortByValue_double(HashMap<String, Double> hm)
    {
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
     *sorteaza alfabetic in functie de prima litera
     */
    static class Sortbynameofratingsasc implements Comparator<Series> {
        public int compare(Series a, Series b)
        {
            return (a.getName().charAt(0) - b.getName().charAt(0));
        }
    }

    /**
     *intoarce lista in functie de tipul sortarii
     */
    public ArrayList<Series> sort(String sorttype, ArrayList<Series> list){
        list.sort(new Sortbynameofratingsasc());
        if(sorttype.equals(Constants.DESC)){
            Collections.reverse(list);
            return list;
        }
        return list;
    }

    /**
     *intoarce lista in functie de numarul maxim de elemente
     */
    public ArrayList<Series> number(int number, ArrayList<Series> list){
        if(number < list.size()) {
            ArrayList<Series> temp = new ArrayList<>();
            for (int i = 0; i < number; i++) {
                temp.add(list.get(i));
            }
            list = temp;
        }
        return list;
    }

    /**
     *elimina obiect din lista in functie de an
     */
    public ArrayList<Series> search_by_year(ArrayList<Series> list, String year){
        Integer i = Integer.parseInt(year);
        list.removeIf(series -> !series.getYear().equals(i));
        return list;
    }

    /**
     *elimina obiect din lista in functie de gen
     */
    public ArrayList<Series> search_by_genre(ArrayList<Series> list, String genre){
        list.removeIf(series -> !series.getGenres().contains(genre));
        return list;
    }

    /**
     *intoarce rezultat query
     */
    public ArrayList<Series> get_series_list_by_filters(List<List<String>> filters, ArrayList<Series> series,
                                                      String criteria, int number, String sorttype,
                                                      ArrayList<User> users){
        ArrayList<Series> new_series_list = new ArrayList<>(series);
        switch (criteria) {
            case Constants.FAVORITE -> {
                ArrayList<String> series_in_favorite = new ArrayList<>();
                //adaug toate serialele din listele de favorite
                for (User user : users) {
                    for (Series serie : series) {
                        if (user.getFavourite().contains(serie.getName())) {
                            series_in_favorite.add(serie.getName());
                        }
                    }
                }
                //elimin elemente in functie de filtre
                for (String t : filters.get(0)) {
                    if (t != null) {
                        new_series_list = this.search_by_year(series, t);
                        new_series_list = this.sort(sorttype, new_series_list);
                        new_series_list = this.number(number, new_series_list);
                    }
                }
                for (String t : filters.get(1)) {
                    if (t != null) {
                        new_series_list = this.search_by_genre(series, t);
                        new_series_list = this.sort(sorttype, new_series_list);
                        new_series_list = this.number(number, new_series_list);
                    }
                }
                //adaug intr-o noua lista intersectia dintre lista de seriale si lista de seriale favorite
                ArrayList<Series> temporary = new ArrayList<>();
                for (Series serie : new_series_list) {
                    for (String s : series_in_favorite) {
                        if (serie.getName().equals(s)) {
                            temporary.add(serie);
                        }
                    }
                }
                new_series_list = temporary;
            }
            case Constants.RATINGS -> {
                HashMap<String, Double> series_rating = new HashMap<>();
                for (Series serie : series) {
                    series_rating.put(serie.getName(), serie.getRating());
                }
                //sortez map-ul de seriale si rating-uri
                HashMap<String, Double> series_rating1 = sortByValue_double(series_rating);
                ArrayList<Series> temp1 = new ArrayList<>();
                //elimin daca are rating 0
                for (Map.Entry<String, Double> en : series_rating1.entrySet()) {
                    if (en.getValue() > 0) {
                        Series serie = this.find_serie_by_title(series, en.getKey());
                        temp1.add(serie);
                    }
                }
                new_series_list = temp1;
                //filtez serialele in functie de criterii
                for (String t : filters.get(0)) {
                    if (t != null) {
                        new_series_list = this.search_by_year(new_series_list, t);
                        new_series_list = this.sort(sorttype, new_series_list);
                        new_series_list = this.number(number, new_series_list);
                    }
                }
                for (String t : filters.get(1)) {
                    if (t != null) {
                        new_series_list = this.search_by_genre(new_series_list, t);
                        new_series_list = this.sort(sorttype, new_series_list);
                        new_series_list = this.number(number, new_series_list);
                    }
                }
            }
            case Constants.LONGEST -> {
                HashMap<String, Integer> serie_duration = new HashMap<>();
                for (Series serie : series) {
                    serie_duration.put(serie.getName(), serie.getDuration());
                }
                //sortez in functie de durata serialului
                HashMap<String, Integer> serie_duration1 = sortByValue(serie_duration);
                ArrayList<Series> temp2 = new ArrayList<>();
                for (Map.Entry<String, Integer> en : serie_duration1.entrySet()) {
                    if (en.getValue() > 0) {
                        Series serie = this.find_serie_by_title(series, en.getKey());
                        temp2.add(serie);
                    }
                }
                new_series_list = temp2;
                //filtrez in functie de criterii
                for (String t : filters.get(0)) {
                    if (t != null) {
                        new_series_list = this.search_by_year(new_series_list, t);
                        new_series_list = this.sort(sorttype, new_series_list);
                        new_series_list = this.number(number, new_series_list);
                    }
                }
                for (String t : filters.get(1)) {
                    if (t != null) {
                        new_series_list = this.search_by_genre(new_series_list, t);
                        new_series_list = this.sort(sorttype, new_series_list);
                        new_series_list = this.number(number, new_series_list);
                    }
                }
            }
            case Constants.MOST_VIEWED -> {
                HashMap<String, Integer> series_total_views = new HashMap<>();
                for (Series serie : series) {
                    series_total_views.put(serie.getName(), 0);
                }
                //calculez numarul total de vizualizari per serial
                for (User user : users) {
                    for (Map.Entry<String, Integer> each : user.getHistory().entrySet()) {
                        for (Map.Entry<String, Integer> mov : series_total_views.entrySet()) {
                            if (each.getKey().equals(mov.getKey())) {
                                mov.setValue(mov.getValue() + each.getValue());
                            }
                        }
                    }
                }
                //sortez descrescator in functie de valoare
                HashMap<String, Integer> movie_total_views1 = sortByValue(series_total_views);
                ArrayList<Series> temp3 = new ArrayList<>();
                for (Map.Entry<String, Integer> en : movie_total_views1.entrySet()) {
                    if (en.getValue() > 0) {
                        Series serie = this.find_serie_by_title(series, en.getKey());
                        temp3.add(serie);
                    }
                }
                new_series_list = temp3;
                //elimin in functie de filtre
                for (String t : filters.get(0)) {
                    if (t != null) {
                        new_series_list = this.search_by_year(new_series_list, t);
                        new_series_list = this.sort(sorttype, new_series_list);
                        new_series_list = this.number(number, new_series_list);
                    }
                }
                for (String t : filters.get(1)) {
                    if (t != null) {
                        new_series_list = this.search_by_genre(new_series_list, t);
                        new_series_list = this.sort(sorttype, new_series_list);
                        new_series_list = this.number(number, new_series_list);
                    }
                }
            }
        }
        return new_series_list;
    }
}
