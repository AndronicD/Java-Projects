package implement.giftdistributor;

import enums.Category;
import implement.Child;
import implement.DataTransfer;
import implement.Gift;
import implement.GiftInput;

import java.util.List;

public class Distributor {
    private GiftFinder finder;

    public Distributor() {
        this.finder = new GiftFinder();
    }

    /**
     * getter
     */
    public GiftFinder getFinder() {
        return finder;
    }

    /**
     * setter
     */
    public void setFinder(final GiftFinder finder) {
        this.finder = finder;
    }

    /**
     * distribuie un cadou pentru fiecare preferinta al obiectelor de tip Child
     */
    public void distribute(final List<Child> children, final List<GiftInput> gifts) {
        DataTransfer transferer = new DataTransfer();
        for (Child child : children) {
            Double sum = child.getAssignedBudget();
            for (Category category : child.getGiftsPreferences()) {
                List<GiftInput> temporaryList = finder.findGiftCat(gifts, category);
                GiftInput finalGift = finder.getTheCheapest(temporaryList);
                if (finalGift != null) {
                    if (sum - finalGift.getPrice() > 0.0) {
                        sum -= finalGift.getPrice();
                        Gift newGift = new Gift();
                        transferer.transferGift(finalGift, newGift);
                        child.getReceivedGifts().add(newGift);
                        finalGift.setQuantity(finalGift.getQuantity() - 1);
                    }
                }
            }
        }
    }
}
