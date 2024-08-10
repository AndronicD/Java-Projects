package implement.elfhandler;

import common.Constants;
import implement.Child;

public class Pink implements ElfStrategy {
    /**
     * mareste cu 30% bugetul asignat unui obiectde tip Child
     */
    @Override
    public void setAssignatedBudget(final Child child) {
        child.setAssignedBudget(child.getAssignedBudget()
                + ((child.getAssignedBudget() * Constants.PROCENTCUT) / Constants.PROCENT));
    }
}
