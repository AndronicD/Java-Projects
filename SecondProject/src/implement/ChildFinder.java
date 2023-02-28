package implement;

import implement.reader.ChildInput;

import java.util.List;

public class ChildFinder {
    /**
     * gaseste un obiect de tip ChildInput care are campul de id
     * acelasi ca si obiectul de tip Child dat ca parametru si intoarce
     * niceScoreBonus-ul
     */
    public int findChildByID(final Child child, final List<ChildInput> list) {
        for (ChildInput children : list) {
            if (children.getId().equals(child.getId())) {
                return children.getNiceScoreBonus();
            }
        }
        return 0;
    }

    /**
     *gaseste un obiect de tip ChildInput care are campul de id
     *acelasi ca si obiectul de tip Child dat ca parametru si intoarce
     *referinta acestuia
     */
    public ChildInput findChild(final Child child, final List<ChildInput> list) {
        for (ChildInput children : list) {
            if (children.getId().equals(child.getId())) {
                return children;
            }
        }
        return null;
    }
}
