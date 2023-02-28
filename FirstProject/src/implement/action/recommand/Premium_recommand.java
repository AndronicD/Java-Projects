package implement.action.recommand;

import common.Constants;
import entertainment.Genre;
import implement.Movie;
import implement.Series;
import implement.User;
import implement.Video;

import java.util.*;

public class Premium_recommand extends Recomand {
    public StringBuilder message = new StringBuilder();

    /**
     constructor pentru obiect de tipul recomandare prmeium
     */
    public Premium_recommand() {
    }

    /**
     *sorteaza map in functie de valoarea cheii
     */
    public static LinkedHashMap<String, Integer> sortByValue(LinkedHashMap<String, Integer> hm) {
        List<Map.Entry<String, Integer> > list =
                new LinkedList<>(hm.entrySet());
        list.sort((o1, o2) -> (o2.getValue()).compareTo(o1.getValue()));
        LinkedHashMap<String, Integer> temp = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    /**
     *intoarce mesaj pentru output
     */
    public StringBuilder get_message(String video, String type) {
        if (type.equals("popular")) {
            this.message.append("Popular");
        }
        else if (type.equals("favorite")) {
            this.message.append("Favorite");
        }
        else {
            this.message.append("Search");
        }
        if (video == null) {
            this.message.append("Recommendation cannot be applied!");
            return this.message;
        }
        this.message.append("Recommendation result: ");
        this.message.append(video);
        return this.message;
    }

    /**
     *sterge din lista un obiect de tip string
     */
    public void remove_from_list(ArrayList<String> list, String title){
        list.removeIf(s -> s.equals(title));
    }

    /**
     *intoarce valoarea de adevar daca un stirng se afla in lista
     */
    public boolean title_found_in_list(ArrayList<String> list, String title) {
        for (String s : list) {
            if (s.equals(title)) return true;
        }
        return false;
    }

    /**
     *intoarce user-ul cu numele dat ca parametru
     */
    public User find_user_by_name(ArrayList<User> users, String name) {
        for (User user : users) {
            if (user.getName().equals(name)) {
                return user;
            }
        }
        return null;
    }

    /**
     *intoarce rezultatul pentru fiecare tip de recomandare premium
     */
    public String get_premium_recommand(String user_name, ArrayList<Movie> movies, ArrayList<Series> series,
                                         String type, ArrayList<User> users, String genre) {
        User user = this.find_user_by_name(users, user_name);
        if (user.getSubscription().equals(Constants.BASIC)) {
            return null;
        }
        String result_recommand = "";
        ArrayList<String> videos_seen_by_user = new ArrayList<>();
        switch(type) {
            case Constants.POPULAR -> {
                LinkedHashMap<String, Integer> popular_genre_map = new LinkedHashMap<>();
                //initializez map de genuri
                for (Genre gen : Genre.values()) {
                    switch(gen) {
                        case ACTION_ADVENTURE -> popular_genre_map.put("ACTION & ADVENTURE", 0);
                        case TV_MOVIE -> popular_genre_map.put("TV MOVIE", 0);
                        case SCI_FI_FANTASY -> popular_genre_map.put("SCI-FI & FANTASY", 0);
                        case SCIENCE_FICTION -> popular_genre_map.put("SCIENCE FICTION", 0);
                    }
                    popular_genre_map.put(gen.toString(), 0);
                }
                //adun la valorile cheilor numarul de aparitii
                for (Movie movie : movies) {
                    for (String s : movie.getGenres()) {
                        popular_genre_map.put(s.toUpperCase(), popular_genre_map.get(s.toUpperCase()) + 1);
                    }
                }
                for (Series serie : series) {
                    for (String s : serie.getGenres()) {
                        popular_genre_map.put(s.toUpperCase(), popular_genre_map.get(s.toUpperCase()) + 1);
                    }
                }
                //sortez descrescator in functie de popularitatea genului
                LinkedHashMap<String, Integer> popular_genre_map1 = sortByValue(popular_genre_map);
                Map.Entry<String,Integer> entry = popular_genre_map1.entrySet().iterator().next();
                String popular_genre = entry.getKey();
                for (Map.Entry<String, Integer> each : user.getHistory().entrySet()) {
                    videos_seen_by_user.add(each.getKey());
                }
                ArrayList<String> videos_in_database = new ArrayList<>();
                for (Movie movie : movies) {
                    videos_in_database.add(movie.getName());
                }
                for (Series serie : series) {
                    videos_in_database.add(serie.getName());
                }
                //elimin video-uri deja vazute de utilizator
                for (String t : videos_seen_by_user) {
                    remove_from_list(videos_in_database, t);
                }
                int found = 0;
                //intorc primul film ce are genul cel mai popular
                for (String s : videos_in_database) {
                    for (Movie movie : movies) {
                        if (movie.getName().equals(s)) {
                            for (String gen : movie.getGenres()) {
                                if (gen.toUpperCase().equals(popular_genre)) {
                                    result_recommand = s;
                                    found = 1;
                                    break;
                                }
                            }
                        }
                    }
                    for (Series serie : series) {
                        if (serie.getName().equals(s)) {
                            for (String gen : serie.getGenres()) {
                                if (gen.toUpperCase().equals(popular_genre)) {
                                    result_recommand = s;
                                    found = 1;
                                    break;
                                }
                            }
                        }
                    }
                    if (found == 1) break;
                }
            }
            case Constants.FAVORITE -> {
                LinkedHashMap<String, Integer> list_fav_video = new LinkedHashMap<>();
                //initializez map pentru filme si cate aparitii au in listele de favorite
                for (Movie movie : movies) {
                    list_fav_video.put(movie.getName(), 0);
                }
                for (Series serie : series) {
                    list_fav_video.put(serie.getName(), 0);
                }
                for (User user1 : users) {
                    for (Map.Entry<String, Integer> each : user1.getHistory().entrySet()) {
                        list_fav_video.put(each.getKey(), each.getValue() + 1);
                    }
                }
                for (Map.Entry<String, Integer> each : user.getHistory().entrySet()) {
                    videos_seen_by_user.add(each.getKey());
                }
                //adaug toate video-urile din baza de date
                ArrayList<String> videos_in_database = new ArrayList<>();
                for (Movie movie : movies) {
                    videos_in_database.add(movie.getName());
                }
                for (Series serie : series) {
                    videos_in_database.add(serie.getName());
                }
                //elimin video-uri deja vazute
                for (String t : videos_seen_by_user) {
                    remove_from_list(videos_in_database, t);
                }
                //sortez lista de filme favorite descrescator
                LinkedHashMap<String, Integer> list_fav_video1 = sortByValue(list_fav_video);
                list_fav_video1.values().removeIf(value -> value.equals(0));
                for (Map.Entry<String, Integer> each : list_fav_video1.entrySet()) {
                    if (title_found_in_list(videos_in_database, each.getKey())) {
                        result_recommand = each.getKey();
                    }
                }
                //intorc prima valoare si daca aceasta nu exista null
                if (result_recommand.equals("")) result_recommand = null;
            }
            case Constants.SEARCH -> {
                ArrayList<Video> videos_in_database = new ArrayList<>(movies);
                videos_in_database.addAll(series);
                ArrayList<Video> videos_in_database_genre = new ArrayList<>();
                for (Video video : videos_in_database) {
                    for (String genres : video.getGenres()) {
                        if (genres.equals(genre)) videos_in_database_genre.add(video);
                    }
                }
                //daca criteriul este gresit intorc null
                if (videos_in_database_genre.isEmpty()) result_recommand = null;
            }
            default -> {
            }
        }
        return result_recommand;
    }
}
