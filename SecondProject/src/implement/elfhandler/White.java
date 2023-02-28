package implement.elfhandler;

import implement.Child;

public class White implements ElfStrategy {
    /**
     * bugetul asignat unui obiect de tip Child ramane cel deja asignat
     */
    @Override
    public void setAssignatedBudget(final Child child) {
        child.setAssignedBudget(child.getAssignedBudget());
    }
}
