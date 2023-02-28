package implement.action.query;

import common.Constants;
import implement.User;

import java.util.ArrayList;
import java.util.Comparator;

public class User_Query extends Query{
    public StringBuilder message;
    public User_Query(){
        message = new StringBuilder();
    }

    /**
     *sorteaza crescator in functie de numarul de rating-uri
     */
    static class Sortbynumberofratingsasc implements Comparator<User> {
        public int compare(User a, User b)
        {
            return a.getNumber_of_ratings() - b.getNumber_of_ratings();
        }
    }

    /**
     *sorteaza descrescator in functie de numarul de rating-uri
     */
    static class Sortbynumberofratingsdesc implements Comparator<User> {
        public int compare(User a, User b)
        {
            return b.getNumber_of_ratings() - a.getNumber_of_ratings();
        }
    }

    /**
     *intoarce lista pentru query
     */
    public ArrayList<User> get_list(ArrayList<User> list, String sorttype, int number){
        int iter = 0;
        //sortez in functie de numarul de rateing-uri date
        if(sorttype.equals(Constants.ASC))
            list.sort(new Sortbynumberofratingsasc());
        else
            list.sort(new Sortbynumberofratingsdesc());
        ArrayList<User> new_list = new ArrayList<>();
        //in functie de numarul dat ca input creez o noua lista cu numarul de elemente cerute
        if(list.size() > number)
            while(iter < number) {
                if (list.get(iter).getNumber_of_ratings() > 0) {
                    new_list.add(list.get(iter));
                }
                iter++;
            }
        else{
            while(iter < list.size()){
                if(list.get(iter).getNumber_of_ratings() > 0) {
                    new_list.add(list.get(iter));
                }
                iter++;
            }
        }
        return new_list;
    }

    /**
     *intoarce mesajul pentur output
     */
    public StringBuilder get_message(ArrayList<User> users) {
        this.message.append("Query result: ");
        this.message.append("[");
        for(int i = 0; i < users.size(); i++) {
            if(i < users.size()-1) {
                this.message.append(users.get(i).getName());
                this.message.append(", ");
            }
            else{
                this.message.append(users.get(i).getName());
            }
        }
        this.message.append("]");
        return this.message;
    }
}
