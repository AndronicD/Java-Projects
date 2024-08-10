import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class Tema2 {
    public static Integer NR_THREADS;
    public static File Folder;
    public static File ProdFile;
    public static File OrdFile;

    public static File ProdFileFin;
    public static File OrdFileFin;

    public static Semaphore semaphore;

    public static void main(String[] args) throws FileNotFoundException {
        File[] Files = new File[2];

        NR_THREADS = Integer.parseInt(args[1]);
        semaphore = new Semaphore(NR_THREADS);

        Folder = new File(args[0]);
        int i = 0;
        for(File file : Folder.listFiles()){
            Files[i] = file;
            i++;
        }

        ProdFile = Files[1];
        OrdFile = Files[0];

        ProdFileFin = new File("order_products_out.txt");
        OrdFileFin = new File("orders_out.txt");
        
        try {
            new FileWriter("order_products_out.txt", false).close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            new FileWriter("orders_out.txt", false).close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        AtomicInteger lineNumber = new AtomicInteger(0);
        ExecutorService tpe = Executors.newFixedThreadPool(NR_THREADS);

        tpe.submit(new OrderRunnable(tpe, lineNumber));
    }
}

