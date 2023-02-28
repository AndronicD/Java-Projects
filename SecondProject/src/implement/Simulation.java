package implement;

import common.Constants;
import implement.agehandler.Handler;
import implement.calculator.Calculator;
import implement.elfhandler.ElfHandler;
import implement.giftdistributor.Distributor;
import implement.nicescorebonushandler.NiceScoreBonus;
import implement.output_writer.Writer;
import implement.reader.Input;
import implement.reader.Reader;
import implement.strategy.SortHandler;
import implement.updater.Updater;
import implement.yellowelf.YellowElf;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class Simulation {
    /**
     * simuleaza distribuirea cadourilor si actualizarile datelor
     */
    public void simulation(final File inputFile, final Integer testNumber) throws IOException {
        Reader reader = Reader.getInstance();
        Input inputData = new Input();
        inputData = reader.readData(inputFile, inputData);
        DataTransfer transfer = new DataTransfer();
        ChildrenList childrenList = new ChildrenList();
        ChildrenOutput childrenOutput = new ChildrenOutput();
        ChildFinder finder = new ChildFinder();
        Handler handler = new Handler();
        ElfHandler elfHandler = new ElfHandler();
        Calculator calculator = new Calculator();
        Distributor distributor = new Distributor();
        transfer.transferData(inputData.getInitialData().getChildren(), childrenList.getChildren());
        Santa santa = new Santa();
        santa.setSantaBudget(inputData.getSantaBudget().doubleValue());
        Writer writer = Writer.getInstance();
        YellowElf yellowElf = new YellowElf();
        SortHandler sortHandler = new SortHandler();
        Path outputPath = Path.of(Constants.OUTPUT_PATH + testNumber + Constants.FILE_EXTENSION);
        File outputFile = new File(String.valueOf(outputPath));

        for (int i = 0; i <= inputData.getNumberOfYears(); i++) {
            if (i > 0) {
                for (Child child : childrenList.getChildren()) {
                    child.setAge(child.getAge() + 1);
                }
                handler.handleAge(childrenList.getChildren());
                for (Child child : childrenList.getChildren()) {
                    child.getReceivedGifts().removeAll(child.getReceivedGifts());
                }

                AnnualChange change = inputData.getAnnualChanges().get(i - 1);
                Updater updater = new Updater(change);
                if (change.getNewSantaBudget() != null) {
                    updater.updateBudget(santa);
                }
                if (change.getNewGifts() != null) {
                    updater.updateGifts(inputData.getInitialData().getSantaGiftsList());
                }
                if (change.getNewChildren() != null) {
                    updater.updateChildrenList(childrenList.getChildren(),
                            inputData.getInitialData().getChildren());
                }
                if (change.getChildrenUpdates() != null) {
                    updater.updateChildrenInfo(childrenList.getChildren(),
                            inputData.getInitialData().getChildren());
                }
            }

            Double sum;
            handler.handleAge(childrenList.getChildren());

            for (Child child : childrenList.getChildren()) {
                NiceScoreBonus bonus = new NiceScoreBonus.Builder(
                        finder.findChildByID(child,
                                inputData.getInitialData().getChildren())).build();
                bonus.giveBonus(child);
            }

            sum = calculator.sumOfAvrages(childrenList.getChildren());
            calculator.calculateUnit(sum, santa);

            for (Child child : childrenList.getChildren()) {
                calculator.calculateEachBudget(child, santa);
            }

            elfHandler.handleElf(childrenList.getChildren(),
                    inputData.getInitialData().getChildren());

            if (i > 0) {
                AnnualChange change = inputData.getAnnualChanges().get(i - 1);
                childrenList.setChildren(sortHandler.handleSort(childrenList.getChildren(),
                        change.getStrategy()));
            }

            distributor.distribute(childrenList.getChildren(),
                    inputData.getInitialData().getSantaGiftsList());

            yellowElf.yellowElf(childrenList.getChildren(),
                    inputData.getInitialData().getChildren(),
                    inputData.getInitialData().getSantaGiftsList());

            childrenList.setChildren(sortHandler.handleSort(childrenList.getChildren(), "id"));

            ChildrenList copy = new ChildrenList();
            transfer.copyData(childrenList, copy);
            childrenOutput.getAnnualChildren().add(copy);
            writer.writeData(outputFile, childrenOutput);
        }
    }
}
