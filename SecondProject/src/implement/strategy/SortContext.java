package implement.strategy;

import implement.Child;

import java.util.List;

public class SortContext {
    private SortStrategy strategy;

    public SortContext(final SortStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * executa strategia
     */
    public List<Child> executeStrategy(final List<Child> childList) {
        return this.strategy.sort(childList);
    }
}
