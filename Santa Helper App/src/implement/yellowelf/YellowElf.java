package implement.yellowelf;

import common.Constants;
import implement.*;
import implement.giftdistributor.GiftFinder;
import implement.reader.ChildInput;

import java.util.List;

public class YellowElf {
    /**
     * gaseste pentru fiecare obiect de tip ChildInput daca acesta are
     * campul elf setat ca yellow, gaseste cel mai ieftin cadou din categoria
     * preferata daca nu a primit niciun cadou, doar daca cantitatea acelui
     * cadou este mai mare decat 0
     */
    public void yellowElf(final List<Child> childList,
                          final List<ChildInput> childInputs,
                          final List<GiftInput> santaGifts) {
        ChildFinder finder = new ChildFinder();
        GiftFinder giftFinder = new GiftFinder();
        DataTransfer transfer = new DataTransfer();
        for (Child child : childList) {
            ChildInput childInput = finder.findChild(child, childInputs);
            if (childInput != null) {
                if (childInput.getElf().equals(Constants.YELLOW)) {
                    if (child.getReceivedGifts().size() == 0) {
                        List<GiftInput> temporaryList = giftFinder.findYellowGiftCat(santaGifts,
                                child.getGiftsPreferences().get(0));
                        GiftInput finalGift = giftFinder.getTheCheapest(temporaryList);
                        if (finalGift != null && finalGift.getQuantity() > 0) {
                            Gift newGift = new Gift();
                            transfer.transferGift(finalGift, newGift);
                            child.getReceivedGifts().add(newGift);
                            finalGift.setQuantity(finalGift.getQuantity() - 1);
                        }
                    }
                }
            }
        }
    }
}
