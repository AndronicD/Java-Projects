package implement.updater;

import implement.Santa;
import lombok.Data;

@Data
public class UpdateBudget implements Update {
    private Updater update;
    private Santa santa;

    /**
     * constructor
     */
    public UpdateBudget(final Santa santa) {
        this.santa = santa;
    }

    /**
     * update al bugetului unui obiect Santa
     */
    @Override
    public void update() {
        this.update.updateBudget(santa);
    }
}
