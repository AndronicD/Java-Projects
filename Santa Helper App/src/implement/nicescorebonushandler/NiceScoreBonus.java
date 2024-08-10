package implement.nicescorebonushandler;

import common.Constants;
import implement.Child;

public class NiceScoreBonus {
    private Integer value;

    private NiceScoreBonus(final Builder builder) {
        this.value = builder.value;
    }

    /**
     * getter
     */
    public int getValue() {
        return value;
    }

    public static class Builder {
        private Integer value;

        /**
         * initializeaza campul value
         */
        public Builder(final Integer value) {
            this.value = value;
        }

        /**
         * intoarce referinta la tipul de obiect NiceScoreBonus
         */
        public NiceScoreBonus build() {
            return new NiceScoreBonus(this);
        }
    }

    /**
     * acorda bonus-ul de cumintenie, iar daca valoarea aceasta
     * depaseste valoarea 10.0, o trunchiaza
     */
    public void giveBonus(final Child child) {
        Double finalAverageScore = child.getAverageScore()
                + (child.getAverageScore() * this.value) / Constants.PROCENT;
        if (finalAverageScore > Constants.MAXIMSCORE) {
            finalAverageScore = Constants.MAXIMSCORE;
        }
        child.setAverageScore(finalAverageScore);
    }
}
