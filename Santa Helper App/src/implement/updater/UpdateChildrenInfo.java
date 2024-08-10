package implement.updater;

import implement.Child;
import implement.reader.ChildInput;
import lombok.Data;

import java.util.List;

@Data
public class UpdateChildrenInfo implements Update {
    private Updater update;
    private List<Child> children;
    private List<ChildInput> childInputList;

    /**
     * constructor
     */
    public UpdateChildrenInfo(final List<Child> children) {
        this.children = children;
    }

    /**
     * update pentru informatiile retinute in lista de obiecte Child
     */
    @Override
    public void update() {
        update.updateChildrenInfo(this.children, this.childInputList);
    }
}
