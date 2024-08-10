package implement.action.command;

import fileio.Writer;
import implement.Movie;
import implement.Series;
import implement.User;
import org.json.simple.JSONArray;
import java.io.IOException;
import java.util.ArrayList;

abstract class Command {
    public User search_for_user(String name, ArrayList<User> list) {
        for (User user : list) {
            if (user.getName().equals(name)) {
                return user;
            }
        }
        return null;
    }

    public Movie search_for_movie(String name, ArrayList<Movie> list) {
        for (Movie movie : list) {
            if (movie.getName().equals(name)) {
                return movie;
            }
        }
        return null;
    }

    public Series search_for_series(String name, ArrayList<Series> list) {
        for (Series series : list) {
            if (series.getName().equals(name)) {
                return series;
            }
        }
        return null;
    }

    /**
     *functie pentru a adauga rezultatul in fisierul de output
     */
    public void add_in_file(int id, JSONArray array, Writer writer, StringBuilder message) throws IOException {
        array.add(writer.writeFile(id, "", message.toString()));
    }
}
