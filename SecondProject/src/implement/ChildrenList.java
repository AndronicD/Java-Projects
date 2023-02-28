package implement;

import java.util.ArrayList;
import java.util.List;

public class ChildrenList {
    private List<Child> children;

    public ChildrenList() {
        this.children = new ArrayList<>();
    }

    /**
     * getter
     */
    public List<Child> getChildren() {
        return children;
    }

    /**
     * setter
     */
    public void setChildren(final List<Child> children) {
        this.children = children;
    }
}
