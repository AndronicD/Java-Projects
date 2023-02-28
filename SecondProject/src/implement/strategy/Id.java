package implement.strategy;

import implement.Child;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Id implements SortStrategy {
    /**
     * sorteaza lista de copii in functie de ID
     */
    @Override
    public List<Child> sort(final List<Child> childList) {
        Collections.sort(childList, new Comparator<Child>() {
            @Override
            public int compare(final Child o1, final Child o2) {
                return o1.getId() - o2.getId();
            }
        });
        return childList;
    }
}
