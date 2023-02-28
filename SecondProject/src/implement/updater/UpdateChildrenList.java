package implement.updater;

import implement.Child;
import implement.reader.ChildInput;

import java.util.List;

public class UpdateChildrenList implements Update {
    private Updater update;
    private List<Child> children;
    private List<ChildInput> childInputs;

    /**
     * constructor
     */
    public UpdateChildrenList(final List<Child> children) {
        this.children = children;
    }

    /**
     * update pentru lista de obiecte Child
     */
    @Override
    public void update() {
        update.updateChildrenList(this.children, this.childInputs);
    }
}
