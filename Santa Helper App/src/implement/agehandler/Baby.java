package implement.agehandler;

import common.Constants;
import implement.Child;

public class Baby implements Strategy {
    /**
     * seteaza average score-ul pentru un obiect Child ce are varsta mai mica de 5 ani
     */
    @Override
    public void setAverageScore(final Child child) {
        child.setAverageScore(Constants.SCORE_FOR_BABY);
    }
}
