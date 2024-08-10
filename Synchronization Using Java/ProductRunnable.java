import java.io.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ProductRunnable implements Runnable{
    private final AtomicInteger lineNumCurr;
    private final Order order;

    public ProductRunnable(AtomicInteger lineNumCurr, Order order) {
        this.lineNumCurr = lineNumCurr;
        this.order = order;
    }

    @Override
    public void run() {
        try {
            Tema2.semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Product currentProduct;
        String lineOrder = null;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(Tema2.ProdFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        synchronized (OrderRunnable.class) {
            for (int i = 0; i < lineNumCurr.get(); i++) {
                try {
                    reader.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            while (true) {
                try {
                    lineOrder = reader.readLine();
                    if (lineOrder == null) {
                        break;
                    }
                    if (lineOrder.substring(0, 12).equals(order.getID())) {
                        lineNumCurr.incrementAndGet();
                        break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                lineNumCurr.incrementAndGet();
            }
        }
        if (lineOrder != null) {
            currentProduct = new Product(lineOrder);

            FileWriter myWriter = null;

            try {
                myWriter = new FileWriter(Tema2.ProdFileFin, true);
            } catch (IOException e) {
                e.printStackTrace();
            }

            synchronized (OrderRunnable.class) {
                try {
                    myWriter.write(currentProduct.getOrderName() + "," + currentProduct.getProductName() + ",shipped" + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                myWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Tema2.semaphore.release();
    }
}
