package implement.agehandler;

import implement.Child;

public class Teen implements Strategy {

    /**
     * seteaza average score-ul pentru un obiect Child ce are varsta intre 12 si 18 ani
     */
    @Override
    public void setAverageScore(final Child child) {
        Double score = (double) 0;
        Double sum = (double) 0;
        for (int i = 0; i < child.getNiceScoreHistory().size(); i++) {
            score += (i + 1) * child.getNiceScoreHistory().get(i);
            sum += i + 1;
        }
        child.setAverageScore(score / sum);
    }
}
