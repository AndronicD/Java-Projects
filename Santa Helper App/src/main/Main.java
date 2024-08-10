package main;

import checker.Checker;
import common.Constants;
import implement.Simulation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Class used to run the code
 */
public final class Main {

    private Main() {
        ///constructor for checkstyle
    }
    /**
     * This method is used to call the checker which calculates the score
     * @param args
     *          the arguments used to call the main method
     */
    public static void main(final String[] args) throws IOException {
        String dir = "output";
        File directory = new File(dir);
        directory.mkdir();
        //creeaza fisierele de output
        for (int i = 1; i <= Constants.TESTS_NUMBER; i++) {
            String pathName = Constants.OUTPUT_PATH + i + Constants.FILE_EXTENSION;
            try {
                FileWriter file = new FileWriter(pathName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //pentru fiecare test creeaza o simulare
        for (int i = 1; i <= Constants.TESTS_NUMBER; i++) {
            String pathName = Constants.TEST_PATH + i + Constants.FILE_EXTENSION;
            File inputFile = new File(pathName);
            Simulation simulation = new Simulation();
            simulation.simulation(inputFile, i);
        }
        Checker.calculateScore();
    }
}
