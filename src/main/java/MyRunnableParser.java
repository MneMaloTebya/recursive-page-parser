
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ForkJoinPool;

public class MyRunnableParser implements Runnable{

    private RecursiveParser recursiveParser;
    private Site site;
    private PageParser pageParser;

    public MyRunnableParser(String defaultUrl) {
        site = new Site(defaultUrl);
        pageParser = new PageParser(site);
        try {
            recursiveParser = new RecursiveParser(pageParser.parsing(defaultUrl), pageParser);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        new ForkJoinPool().invoke(recursiveParser);
    }

    @Override
    public void run() {
        long start = System.currentTimeMillis();
        try {
            FileWriter fileWriter = new FileWriter("data/" + site.cutDomain()+ ".txt");
            fileWriter.write(pageParser.getStringBuilder().toString());
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        recursiveParser.compute();
        System.out.println("Поток закончил работу: " + Thread.currentThread().getName());
        System.out.println("Время работы потока: " + (System.currentTimeMillis() - start)/1000 + " сек.");
    }
}
