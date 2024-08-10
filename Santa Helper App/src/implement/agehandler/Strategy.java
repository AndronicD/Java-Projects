package implement.agehandler;

import implement.Child;

public interface Strategy {
    /**
     * metoda ce seteaza averageScore-ul pentru un obiect Child dat ca parametru
     */
    void setAverageScore(Child child);
}
