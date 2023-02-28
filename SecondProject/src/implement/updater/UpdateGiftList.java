package implement.updater;

import implement.GiftInput;

import java.util.List;

public class UpdateGiftList implements Update {
    private Updater update;
    private List<GiftInput> gifts;

    /**
     * constructor
     */
    public UpdateGiftList(final List<GiftInput> gifts) {
        this.gifts = gifts;
    }

    /**
     * update pentru lista de Gift
     */
    @Override
    public void update() {
        this.update.updateGifts(this.gifts);
    }
}
