package implement.elfhandler;

import common.Constants;
import implement.Child;
import implement.ChildFinder;
import implement.reader.ChildInput;

import java.util.List;

public class ElfHandler {
    /**
     * gasesc un obiect de tip Child in lista da date initiale
     * si execut strategia in functie de elf
     */
    public void handleElf(final List<Child> childList, final List<ChildInput> childInputs) {
        ChildFinder finder = new ChildFinder();
        for (Child child : childList) {
            ChildInput childInput = finder.findChild(child, childInputs);
            if (childInput != null) {
                if (childInput.getElf().equals(Constants.PINK)) {
                    ElfContext context = new ElfContext(new Pink());
                    context.executeStrategy(child);
                }
                if (childInput.getElf().equals(Constants.BLACK)) {
                    ElfContext context = new ElfContext(new Black());
                    context.executeStrategy(child);
                }
                if (childInput.getElf().equals(Constants.WHITE)) {
                    ElfContext context = new ElfContext(new White());
                    context.executeStrategy(child);
                }
                if (childInput.getElf().equals(Constants.YELLOW)) {
                    continue;
                }
            }
        }
    }
}
