package implement.action.query;

import actor.ActorsAwards;
import common.Constants;
import implement.Actor;
import implement.Movie;
import implement.Series;
import java.util.*;

public class Actor_Query extends Query {
    public StringBuilder message = new StringBuilder();

    /**
     *constructor action query
     */
    public Actor_Query() {
    }

    /**
     *intoarce mesajul pentur output
     */
    public StringBuilder get_message(ArrayList<Actor> actors) {
        this.message.append("Query result: ");
        this.message.append("[");
        for (int i = 0; i < actors.size(); i++) {
            if (i < actors.size()-1) {
                this.message.append(actors.get(i).getName());
                this.message.append(", ");
            }
            else {
                this.message.append(actors.get(i).getName());
            }
        }
        this.message.append("]");
        return this.message;
    }

    /**
     *sorteaza crescator in functie de primul caracter din string
     */
    static class Sortbynameofratingsasc implements Comparator<Actor> {
        public int compare(Actor a, Actor b)
        {
            return (a.getName().charAt(0) - b.getName().charAt(0));
        }
    }

    /**
     *sorteaza in functie tipul de sortare
     */
    public ArrayList<Actor> sort(String sorttype, ArrayList<Actor> list){
       list.sort(new Sortbynameofratingsasc());
        if (sorttype.equals(Constants.DESC)) {
            Collections.reverse(list);
            return list;
        }
        return list;
    }

    /**
     *sorteaza in functie de numarul dat ca parametru
     */
    public ArrayList<Actor> number(int number, ArrayList<Actor> list) {
        if (number < list.size()) {
            ArrayList<Actor> temp = new ArrayList<>();
            for (int i = 0; i < number; i++) {
                temp.add(list.get(i));
            }
            list = temp;
        }
        return list;
    }

    /**
     *sterge din lista daca nu gaseste cuvantul in descrierea actorului
     */
    public ArrayList<Actor> search_by_words(ArrayList<Actor> list, String word) {
        list.removeIf(actor -> !Arrays.asList(actor.getDescription().toLowerCase().split(" ")).contains(word));
        return list;
    }

    /**
     *elimina din lista daca actorul nu are premiul dat ca parametru
     */
    public ArrayList<Actor> search_by_awards(ArrayList<Actor> list, String award) {
        list.removeIf(actor -> !actor.getAwards().containsKey(ActorsAwards.valueOf(award)));
        return list;
    }

    /**
     *sorteaza crescator in functie de rating-ul filmografiei
     */
    static class Sortbynumberofratingsasc implements Comparator<Actor> {
        public int compare(Actor a, Actor b)
        {
            return (int) (a.filmography_rating - b.filmography_rating);
        }
    }

    /**
     *sorteaza descrescator in functie de rating-ul filmografiei
     */
    static class Sortbynumberofratingsdesc implements Comparator<Actor> {
        public int compare(Actor a, Actor b)
        {
            return (int) (b.filmography_rating - a.filmography_rating);
        }
    }

    /**
     *intoarce rezultat query
     */
    public ArrayList<Actor> get_list_by_filters(List<List<String>> filters, ArrayList<Actor> actors, String criteria,
                                                int number, String sorttype, ArrayList<Movie> m, ArrayList<Series> s) {
        ArrayList<Actor> new_list_actors = new ArrayList<>(actors);
        switch (criteria) {
            case Constants.FILTER_DESCRIPTIONS:
                for (String t : filters.get(2)) {
                    //elimin pe cei ce nu au in descriere cuvintele din lista de filtre
                    new_list_actors = this.search_by_words(new_list_actors, t);
                    new_list_actors = this.sort(sorttype, new_list_actors);
                    if (number != 0) {
                        new_list_actors = this.number(number, new_list_actors);
                    }
                }
                break;
            case Constants.AWARDS:
                for (String t : filters.get(3)) {
                    //elimin pe cei ce nu au castigat premiile din lista de filtre
                    new_list_actors = this.search_by_awards(new_list_actors, t);
                    new_list_actors = this.sort(sorttype, new_list_actors);
                    if (number != 0) {
                        new_list_actors = this.number(number, new_list_actors);
                    }
                }
                break;
            case Constants.AVERAGE:
                for (Series series : s) {
                    series.CalcRating();
                }
                for (Actor actor : new_list_actors) {
                    actor.get_rating_by_filmography(m, s);
                }
                if (sorttype.equals(Constants.ASC)) {
                    new_list_actors.sort(new Sortbynumberofratingsasc());
                }
                else {
                    new_list_actors.sort(new Sortbynumberofratingsdesc());
                }
                //daca rating-ul filmografiei este 0 il elimin
                new_list_actors.removeIf(actor -> actor.filmography_rating == 0.0);
                //sortez in functie de nume
                for (int i = 0; i < new_list_actors.size()-1; i++) {
                    if (new_list_actors.get(i).filmography_rating == new_list_actors.get(i+1).filmography_rating) {
                        if (new_list_actors.get(i).getName().charAt(0) > new_list_actors.get(i+1).getName().charAt(0)) {
                            Collections.swap(new_list_actors, i, i+1);
                        }
                    }
                }
                //in functie de numarul dat la input elimin sau nu din lista de actori
                if (number < new_list_actors.size()) {
                    ArrayList<Actor> temp = new ArrayList<>();
                    for (int i = 0; i < number; i++) {
                        temp.add(new_list_actors.get(i));
                    }
                    new_list_actors = temp;
                }
                break;
            default:
                break;
        }
        return new_list_actors;
    }
}
