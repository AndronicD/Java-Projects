package implement.agehandler;

import common.Constants;
import implement.Child;

import java.util.List;

public class Handler {
    /**
     * seteaza toate avrageScore-urile pentru o lista de obiecte Child in functie de varsta lor
     */
    public void handleAge(final List<Child> childList) {
        for (int i = 0; i < childList.size(); i++) {
            if (childList.get(i).getAge() < Constants.BABY_LIMIT) {
                Context context = new Context(new Baby());
                context.executeStrategy(childList.get(i));
            }
            if (childList.get(i).getAge() >= Constants.BABY_LIMIT
                    && childList.get(i).getAge() < Constants.KID_LIMIT) {
                Context context = new Context(new Kid());
                context.executeStrategy(childList.get(i));
            }
            if (childList.get(i).getAge() >= Constants.KID_LIMIT
                    && childList.get(i).getAge() <= Constants.TEEN_LIMIT) {
                Context context = new Context(new Teen());
                context.executeStrategy(childList.get(i));
            }
            if (childList.get(i).getAge() > Constants.TEEN_LIMIT) {
                childList.remove(childList.get(i));
                i--;
            }
        }
    }
}
