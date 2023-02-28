package implement.action.command;

import implement.Movie;
import implement.Series;
import implement.User;

import java.util.HashMap;
import java.util.Map;

public class Rating extends Command {
    static public HashMap<String, String> rates = new HashMap<>();

    /**
     *constructor pentru comanda rating
     */
    public Rating() {
    }
    public StringBuilder message = new StringBuilder();

    /**
     *adauga rating pentru obiect de tip movie
     */
    public void giveRating(User user, Movie title, double grade) {
        if (title == null) {
            return;
        }
        for (Map.Entry<String,String> each : rates.entrySet()) {
            if (each.getKey().equals(user.getName())) {
               if (each.getValue().equals(title.getName())) {
                    this.message.append("error -> ");
                    this.message.append(title.getName());
                    this.message.append(" has been already rated");
                    return;
                }
            }
        }
        for (Map.Entry<String,Integer> each : user.getHistory().entrySet()) {
            if (each.getKey().equals(title.getName())) {
                if (title.getRatings().get(0) == 0.0) {
                    title.getRatings().remove(0);
                }
                title.getRatings().add(grade);
                this.message.append("success -> ");
                this.message.append(title.getName());
                this.message.append(" was rated with ");
                this.message.append(grade);
                this.message.append(" by ");
                this.message.append(user.getName());
                user.setNumber_of_ratings(user.getNumber_of_ratings() + 1);
                rates.put(user.getName(), title.getName());
                return;
            }
        }
            this.message.append("error -> ");
            this.message.append(title.getName());
            this.message.append(" is not seen");
    }

    /**
     *adauga rating pentru obiect de tip serial
     */
    public void giveRating(User user, Series title, double grade, int nr_season) {
        if (title == null) {
            return;
        }
        for (Map.Entry<String,String> each : rates.entrySet()) {
            if (each.getKey().equals(user.getName())) {
                if (each.getValue().equals(title.getName())) {
                    this.message.append("error -> ");
                    this.message.append(title.getName());
                    this.message.append(" has been already rated");
                    return;
                }
            }
        }
        for (Map.Entry<String, Integer> each : user.getHistory().entrySet()) {
            if (each.getKey().equals(title.getName())) {
                if (title.getSeasons().get(nr_season).getRatings().get(0) == 0.0) {
                    title.getSeasons().get(nr_season).getRatings().remove(0);
                }
                title.getSeasons().get(nr_season).getRatings().add(grade);
                this.message.append("success -> ");
                this.message.append(title.getName());
                this.message.append(" was rated with ");
                this.message.append(grade);
                this.message.append(" by ");
                this.message.append(user.getName());
                user.setNumber_of_ratings(user.getNumber_of_ratings() + 1);
                rates.put(user.getName(), title.getName());
                return;
            }
        }
        this.message.append("error -> ");
        this.message.append(title.getName());
        this.message.append(" is not seen");
    }
}
