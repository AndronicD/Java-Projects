package implement.agehandler;

import implement.Child;

public class Context {
    private Strategy strategy;

    public Context(final Strategy strategy) {
        this.strategy = strategy;
    }

    /**
     * executa strategia
     */
    public void executeStrategy(final Child child) {
        this.strategy.setAverageScore(child);
    }

}
