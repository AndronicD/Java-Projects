package implement.agehandler;

import implement.Child;

public class Kid implements Strategy {

    /**
     * seteaza average score-ul pentru un obiect Child ce are varsta intre 5 si 12 ani
     */
    @Override
    public void setAverageScore(final Child child) {
        Double score = (double) 0;
        for (Double value : child.getNiceScoreHistory()) {
            score += value;
        }
        child.setAverageScore(score / child.getNiceScoreHistory().size());
    }
}
