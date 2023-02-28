package implement.calculator;

import implement.Child;
import implement.Santa;

import java.util.List;

public class Calculator {
    /**
     * calculeaza suma tuturor avrageScore ale obiectelor Child \
     * dintr-o lista de obiecte Child data ca parametru
     */
    public Double sumOfAvrages(final List<Child> list) {
        Double sum = (double) 0;
        for (Child child : list) {
            sum += child.getAverageScore();
        }
        return sum;
    }

    /**
     * calculeaza si seteaza unitBudget-ul pentru un obiect de tip Santa
     */
    public void calculateUnit(final Double sum, final Santa santa) {
        santa.setBudgetUnit(santa.getSantaBudget() / sum);
    }

    /**
     *calculeaza si seteaza bugetul asignat pentru obiectul Child dat ca parametru
     */
    public void calculateEachBudget(final Child child, final Santa santa) {
        child.setAssignedBudget(santa.getBudgetUnit() * child.getAverageScore());
    }
}
