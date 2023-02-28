package implement.strategy;

import common.Constants;
import implement.Child;

import java.util.ArrayList;
import java.util.List;

public class SortHandler {
    /**
     * aplica o strategie de sortare
     */
    public List<Child> handleSort(final List<Child> childList,
                                  final String strategy) {
        List<Child> newList = new ArrayList<>();
        if (strategy.equals(Constants.ID)) {
            SortContext context = new SortContext(new Id());
            newList = context.executeStrategy(childList);
        }
        if (strategy.equals(Constants.NICESCORE)) {
            SortContext context = new SortContext(new NiceScore());
            newList = context.executeStrategy(childList);
        }
        if (strategy.equals(Constants.NICESCORECITY)) {
            SortContext context = new SortContext(new NiceScoreCity());
            newList = context.executeStrategy(childList);
        }
        return newList;
    }
}
