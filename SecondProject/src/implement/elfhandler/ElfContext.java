package implement.elfhandler;

import implement.Child;

public class ElfContext {
    private ElfStrategy strategy;

    public ElfContext(final ElfStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * executa strategia
     */
    public void executeStrategy(final Child child) {
        this.strategy.setAssignatedBudget(child);
    }
}
