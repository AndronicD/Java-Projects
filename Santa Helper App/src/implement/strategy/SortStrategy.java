package implement.strategy;

import implement.Child;

import java.util.List;

public interface SortStrategy {
    /**
     * sorteaza o lista de obiecte de tip Child
     */
    List<Child> sort(List<Child> childList);
}
