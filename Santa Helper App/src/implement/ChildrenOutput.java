package implement;

import java.util.ArrayList;
import java.util.List;

public class ChildrenOutput {
    private List<ChildrenList> annualChildren;

    public ChildrenOutput() {
        this.annualChildren = new ArrayList<>();
    }

    /**
     * getter
     */
    public List<ChildrenList> getAnnualChildren() {
        return annualChildren;
    }

    /**
     * setter
     */
    public void setAnnualChildren(final List<ChildrenList> annualChildren) {
        this.annualChildren = annualChildren;
    }
}
