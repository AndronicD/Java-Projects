package implement.giftdistributor;

import enums.Category;
import implement.GiftInput;

import java.util.ArrayList;
import java.util.List;

public class GiftFinder {
    /**
     * intoarce o lista de obiecte GiftInput ce fac parte din categoria data ca parametru
     */
    public List<GiftInput> findGiftCat(final List<GiftInput> gifts, final Category category) {
        List<GiftInput> chosenGifts = new ArrayList<>();
        for (GiftInput gift : gifts) {
            if (gift.getCategory().equals(category)) {
                if (gift.getQuantity() > 0) {
                    chosenGifts.add(gift);
                }
            }
        }
        return chosenGifts;
    }

    /**
     * intoarce o lista de obiecte GiftInput ce fac parte din categoria data ca parametru
     * dar nu se mai tine cont de cantitatea pe care o are acel cadou
     */
    public List<GiftInput> findYellowGiftCat(final List<GiftInput> gifts, final Category category) {
        List<GiftInput> chosenGifts = new ArrayList<>();
        for (GiftInput gift : gifts) {
            if (gift.getCategory().equals(category)) {
                chosenGifts.add(gift);
            }
        }
        return chosenGifts;
    }

    /**
     * intoarce un obiect de tip Gift ce are cel mai mic pret
     */
    public GiftInput getTheCheapest(final List<GiftInput> gifts) {
        if (gifts.size() > 0) {
            GiftInput finalGift = gifts.get(0);
            for (GiftInput gift : gifts) {
                if (gift.getPrice() < finalGift.getPrice()) {
                    finalGift = gift;
                }
            }
            return finalGift;
        }
        return null;
    }
}
