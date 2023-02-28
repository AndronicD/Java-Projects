package implement;

import implement.reader.ChildInput;

import java.util.List;

public class DataTransfer {
    /**
     * creeaza o lista de obiecte de tip Child dintr-o lista de tip ChildInput
     */
    public void transferData(final List<ChildInput> firstList, final List<Child> secondList) {
        for (ChildInput childInput : firstList) {
            Child child = new Child();
            child.setId(childInput.getId());
            child.setAge(childInput.getAge());
            child.getNiceScoreHistory().add(childInput.getNiceScore());
            child.setCity(childInput.getCity());
            child.setFirstName(childInput.getFirstName());
            child.setLastName(childInput.getLastName());
            child.setGiftsPreferences(childInput.getGiftsPreferences());
            secondList.add(child);
        }
    }

    /**
     * copiaza elementele dintr-o lista de tip ChildrenList intr-o alta lista de tip ChildrenList
     */
    public void copyData(final ChildrenList first, final ChildrenList second) {
        for (int i = 0; i < first.getChildren().size(); i++) {
            Child firstChild = first.getChildren().get(i);
            Child secondChild = new Child();
            secondChild.setId(firstChild.getId());
            secondChild.setAge(firstChild.getAge());
            secondChild.setCity(firstChild.getCity());
            secondChild.setFirstName(firstChild.getFirstName());
            secondChild.setLastName(firstChild.getLastName());
            secondChild.setGiftsPreferences(firstChild.getGiftsPreferences());
            secondChild.getReceivedGifts().addAll(firstChild.getReceivedGifts());
            secondChild.setAverageScore(firstChild.getAverageScore());
            secondChild.getNiceScoreHistory().addAll(firstChild.getNiceScoreHistory());
            secondChild.setAssignedBudget(firstChild.getAssignedBudget());
            second.getChildren().add(secondChild);
        }
    }

    /**
     * transfera datele de la un obiect de tip GiftInput la un obiect de tip Gift
     */
    public void transferGift(final GiftInput inputGift, final Gift finalGift) {
        finalGift.setPrice(inputGift.getPrice());
        finalGift.setCategory(inputGift.getCategory());
        finalGift.setProductName(inputGift.getProductName());
    }
}
