
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;

public class MyRunnable implements Runnable{

    private RecursiveParser recursiveParser;

    private Site url;

    public MyRunnable(Site url) {
        this.url = url;
        recursiveParser = new RecursiveParser(RecursiveParser.getUrlsMap());
        try {
            new ForkJoinPool().invoke(new RecursiveParser(PageParser.parsing(Site.getName())));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        try {
            FileWriter fileWriter = new FileWriter("data/" + Site.cutPath() + ".txt");
            fileWriter.write(PageParser.getStringBuilder().toString());
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        recursiveParser.compute();
    }
}
