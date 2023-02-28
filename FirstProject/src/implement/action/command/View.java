package implement.action.command;

import implement.User;
import java.util.Map;

public class View extends Command {
    /**
     *constructor comanda view
     */
    public View() {
    }

    public StringBuilder message = new StringBuilder();

    /**
     *adauga in lista de vizualizari sau incrementeaza numarul de vizualizari
     */
    public void add_in_viewed(User user, String title) {
        for (Map.Entry<String, Integer> each : user.getHistory().entrySet()) {
            if (each.getKey().equals(title)) {
                each.setValue(each.getValue() + 1);
                this.message.append("success -> ");
                this.message.append(title);
                this.message.append(" was viewed with total views of ");
                this.message.append(each.getValue());
                return;
            }
        }
        user.getHistory().put(title, 1);
        this.message.append("success -> ");
        this.message.append(title);
        this.message.append(" was viewed with total views of ");
        this.message.append("1");
    }
}
