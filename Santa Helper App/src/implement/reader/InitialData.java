package implement.reader;

import implement.GiftInput;

import java.util.List;

public class InitialData {
    private List<ChildInput> children;
    private List<GiftInput> santaGiftsList;

    /**
     * getter
     */
    public List<ChildInput> getChildren() {
        return children;
    }

    /**
     * setter
     */
    public void setChildren(final List<ChildInput> children) {
        this.children = children;
    }

    /**
     * getter
     */
    public List<GiftInput> getSantaGiftsList() {
        return santaGiftsList;
    }

    /**
     * setter
     */
    public void setSantaGiftsList(final List<GiftInput> santaGiftsList) {
        this.santaGiftsList = santaGiftsList;
    }
}
