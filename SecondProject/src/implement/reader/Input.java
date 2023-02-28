package implement.reader;

import implement.AnnualChange;

import java.util.List;

public class Input {
    private Integer numberOfYears;
    private Integer santaBudget;
    private InitialData initialData;
    private List<AnnualChange> annualChanges;

    /**
     * getter
     */
    public Integer getNumberOfYears() {
        return numberOfYears;
    }

    /**
     * setter
     */
    public void setNumberOfYears(final Integer numberOfYears) {
        this.numberOfYears = numberOfYears;
    }

    /**
     * getter
     */
    public Integer getSantaBudget() {
        return santaBudget;
    }

    /**
     * setter
     */
    public void setSantaBudget(final Integer santaBudget) {
        this.santaBudget = santaBudget;
    }

    /**
     * getter
     */
    public InitialData getInitialData() {
        return initialData;
    }

    /**
     * setter
     */
    public void setInitialData(final InitialData initialData) {
        this.initialData = initialData;
    }

    /**
     * getter
     */
    public List<AnnualChange> getAnnualChanges() {
        return annualChanges;
    }

    /**
     * setter
     */
    public void setAnnualChanges(final List<AnnualChange> annualChanges) {
        this.annualChanges = annualChanges;
    }
}
