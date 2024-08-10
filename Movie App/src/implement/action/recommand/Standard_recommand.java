package implement.action.recommand;

import common.Constants;
import implement.Movie;
import implement.Series;
import implement.User;
import java.util.*;

public class Standard_recommand extends Recomand {
    public StringBuilder message = new StringBuilder();

    /**
     *consturcor pentur obiect de tip recomandare standard
     */
    public Standard_recommand() {
    }

    /**
     *intoarce mesajul afisat in fiesierul de output
     */
    public StringBuilder get_message(String video, String type) {
        if (type.equals("standard")) {
            this.message.append("Standard");
        }
        else {
            this.message.append("BestRatedUnseen");
        }
        this.message.append("Recommendation result: ");
        this.message.append(video);
        return this.message;
    }

    /**
     *sorteaza un map in functie de valoarea cheii
     */
    public static LinkedHashMap<String, Double> sortByValue_double(LinkedHashMap<String, Double> hm) {
        List<Map.Entry<String, Double> > list =
                new LinkedList<>(hm.entrySet());
        list.sort(Map.Entry.comparingByValue());
        LinkedHashMap<String, Double> temp;
        temp = new LinkedHashMap<>();
        for (Map.Entry<String, Double> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    /**
     *intoarce valoarea de adevar daca un titlu se afla intr-o lista
     */
    public boolean title_found_in_list(ArrayList<String> list, String title) {
        for (String s : list) {
            if (s.equals(title)) return true;
        }
        return false;
    }

    /**
     *intoarce user-ul dintr-o lista de useri
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
     *elimina elementele unei liste daca valoara cheii din map se afla in lista
     */
    public LinkedHashMap<String, Double> elim_from_map(ArrayList<String> list, LinkedHashMap<String, Double> map) {
        for (String s : list) {
            for (Map.Entry<String, Double> ob: map.entrySet()) {
                if (ob.getKey().equals(s)) {
                    map.remove(ob.getKey(),ob.getValue());
                }
                break;
            }
        }
        return map;
    }

    /**
     *intoarce rezultatul pentru fiecare tip de recomandare standard
     */
    public String get_standard_recommand(String user_name, ArrayList<Movie> movies, ArrayList<Series> series,
                                         String type, ArrayList<User> users){
        User user = this.find_user_by_name(users, user_name);
        String result_recommand = "";
        ArrayList<String> videos_seen_by_user = new ArrayList<>();
        for(Map.Entry<String, Integer> each : user.getHistory().entrySet()){
            videos_seen_by_user.add(each.getKey());
        }
        switch (type) {
            case Constants.STANDARD -> {
                ArrayList<String> videos_in_database = new ArrayList<>();
                //adaug toate tipurile de video din input
                for (Movie movie : movies) {
                    videos_in_database.add(movie.getName());
                }
                for (Series serie : series) {
                    videos_in_database.add(serie.getName());
                }
                //elimin pe cele deja vazute de utilizator
                for (String s : videos_in_database) {
                    if (!title_found_in_list(videos_seen_by_user, s)) {
                        result_recommand = s;
                        break;
                    }
                }
                //intorc primul rezultat
            }
            case Constants.BEST_UNSEEN -> {
                //adaug toate videoclipurile din baza de date cu rating
                LinkedHashMap<String, Double> videos_in_database_with_rate = new LinkedHashMap<>();
                for (Movie movie : movies) {
                    videos_in_database_with_rate.put(movie.getName(), movie.getfinalrating(movie.getRatings()));
                }
                for (Series serie : series) {
                    serie.CalcRating();
                    videos_in_database_with_rate.put(serie.getName(), serie.getRating());
                }
                //elimin pe cele deja vazute de utilizator
                videos_in_database_with_rate = this.elim_from_map(videos_seen_by_user, videos_in_database_with_rate);
                //le sortez in functie de valoarea cheii
                LinkedHashMap<String, Double> videos_in_database_with_rate1 =
                        sortByValue_double(videos_in_database_with_rate);
                Map.Entry<String, Double> map = videos_in_database_with_rate1.entrySet().iterator().next();
                //iau prima valoare din map
                result_recommand = map.getKey();
            }
            default -> {
            }
        }
        return result_recommand;
    }
}
