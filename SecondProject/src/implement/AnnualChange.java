package implement;

import implement.reader.ChildInput;

import java.util.List;

public class AnnualChange {
    private Double newSantaBudget;
    private List<GiftInput> newGifts;
    private List<ChildInput> newChildren;
    private List<ChildrenUpdate> childrenUpdates;
    private String strategy;

    /**
     * getter
     */
    public String getStrategy() {
        return strategy;
    }

    /**
     * setter
     */
    public void setStrategy(final String strategy) {
        this.strategy = strategy;
    }

    /**
     * getter
     */
    public Double getNewSantaBudget() {
        return newSantaBudget;
    }

    /**
     * setter
     */
    public void setNewSantaBudget(final Double newSantaBudget) {
        this.newSantaBudget = newSantaBudget;
    }

    /**
     * getter
     */
    public List<GiftInput> getNewGifts() {
        return newGifts;
    }

    /**
     * setter
     */
    public void setNewGifts(final List<GiftInput> newGifts) {
        this.newGifts = newGifts;
    }

    /**
     * getter
     */
    public List<ChildInput> getNewChildren() {
        return newChildren;
    }

    /**
     * setter
     */
    public void setNewChildren(final List<ChildInput> newChildren) {
        this.newChildren = newChildren;
    }

    /**
     * getter
     */
    public List<ChildrenUpdate> getChildrenUpdates() {
        return childrenUpdates;
    }

    /**
     * setter
     */
    public void setChildrenUpdates(final List<ChildrenUpdate> childrenUpdates) {
        this.childrenUpdates = childrenUpdates;
    }
}
