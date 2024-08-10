package implement.updater;

import enums.Category;
import implement.*;
import implement.reader.ChildInput;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Updater {
    private AnnualChange change;

    /**
     * constructor
     */
    public Updater(final AnnualChange change) {
        this.change = change;
    }

    /**
     * seteaza noul buget pentru obiect Santa
     */
    public void updateBudget(final Santa santa) {
        santa.setSantaBudget(this.change.getNewSantaBudget());
    }

    /**
     * adauga toate obiectele de tip Gift intr-o lista data ca parametru
     */
    public void updateGifts(final List<GiftInput> gifts) {
        gifts.addAll(this.change.getNewGifts());
    }

    /**
     * adauga toate obictele de tip Child intr-o lista data ca parametru
     */
    public void updateChildrenList(final List<Child> children, final List<ChildInput> childInputs) {
        DataTransfer transfer = new DataTransfer();
        List<Child> childList = new ArrayList<>();
        transfer.transferData(change.getNewChildren(), childList);
        children.addAll(childList);
        childInputs.addAll(change.getNewChildren());
    }

    /**
     * gaseste un obiect de tip Child in functie de ID
     */
    public Child findChildren(final List<Child> children, final Integer id) {
        for (Child child : children) {
            if (!Objects.equals(child.getId(), id)) {
                continue;
            }
            return child;
        }
        return null;
    }

    /**
     * getter
     */
    public ChildInput findChildrenInput(final List<ChildInput> children,
                                        final Integer id) {
        for (ChildInput child : children) {
            if (!Objects.equals(child.getId(), id)) {
                continue;
            }
            return child;
        }
        return null;
    }

    /**
     * actualizeaza informatiile legate de un obiect de tip Child
     */
    public void updateChildrenInfo(final List<Child> children,
                                   final List<ChildInput> childInputList) {
        for (ChildrenUpdate childUpdate : this.change.getChildrenUpdates()) {
            Child child = findChildren(children, childUpdate.getId());
            if (child != null) {
                if (childUpdate.getNiceScore() != null) {
                    child.getNiceScoreHistory().add(childUpdate.getNiceScore());
                }
                if (childUpdate.getGiftsPreferences() != null) {
                    List<Category> newList = new LinkedList<>();
                    newList.addAll(childUpdate.getGiftsPreferences());
                    newList.addAll(child.getGiftsPreferences());
                    newList = newList.stream().distinct().collect(Collectors.toList());
                    child.setGiftsPreferences(newList);
                }
            }
        }
        for (ChildrenUpdate childUpdate : this.change.getChildrenUpdates()) {
            ChildInput childInput = findChildrenInput(childInputList, childUpdate.getId());
            if (childInput != null) {
                if (childUpdate.getElf() != null) {
                    childInput.setElf(childUpdate.getElf());
                }
                if (childUpdate.getGiftsPreferences() != null) {
                    List<Category> newList = new LinkedList<>();
                    newList.addAll(childUpdate.getGiftsPreferences());
                    newList.addAll(childInput.getGiftsPreferences());
                    newList = newList.stream().distinct().collect(Collectors.toList());
                    childInput.setGiftsPreferences(newList);
                }
            }
        }
    }
}
