package implement.strategy;

import implement.Child;

import java.util.Comparator;
import java.util.List;

public class NiceScore implements SortStrategy {
    /**
     * sorteaza descrescator lista de copii in functie averageScore
     * iar daca au aceeasi valoare al averageScore sorteaza crescator
     * in functie de id
     */
    @Override
    public List<Child> sort(final List<Child> childList) {
        childList.sort(new Comparator<Child>() {
            @Override
            public int compare(final Child o1, final Child o2) {
                if (o1.getAverageScore().equals(o2.getAverageScore())) {
                    return o1.getId() - o2.getId();
                }
                return Double.compare(o2.getAverageScore(), o1.getAverageScore());
            }
        });
        return childList;
    }
}
