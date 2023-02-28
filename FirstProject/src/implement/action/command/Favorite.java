package implement.action.command;

import implement.User;
import java.util.Map;

public class Favorite extends Command {
    /**
     *constructor pentru comanda de tip favorite
     */
    public Favorite() {
    }
    public StringBuilder message = new StringBuilder("");

    /**
     *adauga in lista de favorite ale utilizatorului
     */
    public void add_in_favorite(User user, String title) {
        for (Map.Entry<String, Integer> each : user.getHistory().entrySet()) {
            for (String fav_movies : user.getFavourite()) {
                if (fav_movies.equals(title)) {
                    this.message.append("error -> ");
                    this.message.append(title);
                    this.message.append(" is already in favourite list");
                    return;
                }
            }
            if (each.getKey().equals(title)) {
                user.getFavourite().add(title);
                this.message.append("success -> ");
                this.message.append(title);
                this.message.append(" was added as favourite");
                return;
            }
        }
        this.message.append("error -> ");
        this.message.append(title);
        this.message.append(" is not seen");
    }
}
