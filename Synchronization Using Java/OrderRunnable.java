import java.io.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

public class OrderRunnable implements Runnable{
    private final ExecutorService tpe;
    private final AtomicInteger lineNumber;

    public OrderRunnable(ExecutorService tpe, AtomicInteger lineNumber) {
        this.tpe = tpe;
        this.lineNumber = lineNumber;
    }

    @Override
    public void run() {
        Order currentOrder;
        String lineOrder = null;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(Tema2.OrdFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < lineNumber.get(); i++){
            try {
                reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            lineOrder = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(lineOrder != null) {
            lineNumber.incrementAndGet();
            tpe.submit(new OrderRunnable(tpe, lineNumber));

            currentOrder = new Order(lineOrder);

            Thread[] threads = new Thread[currentOrder.getNumProd()];
            AtomicInteger lineNumCurr = new AtomicInteger(0);

            for (int i = 0; i < currentOrder.getNumProd(); i++) {
                threads[i] = new Thread(new ProductRunnable(lineNumCurr, currentOrder));
                threads[i].start();
            }

            for (int i = 0; i < currentOrder.getNumProd(); i++) {
                try {
                    threads[i].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


            FileWriter myWriter = null;

            try {
                myWriter = new FileWriter(Tema2.OrdFileFin, true);
            } catch (IOException e) {
                e.printStackTrace();
            }

            synchronized (this) {
                if (currentOrder.getNumProd() > 0) {
                    try {
                        myWriter.write(currentOrder.getID() + "," + currentOrder.getNumProd() + ",shipped" + "\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            try {
                myWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else{
            tpe.shutdown();
        }
    }
}