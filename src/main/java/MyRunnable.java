
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;

public class MyRunnable implements Runnable{

    private RecursiveParser recursiveParser;

    private static String defaultUrl = "https://skillbox.ru/";

    private static Set<String> siteTreeSet = new LinkedHashSet<>();

    public static String getDefaultUrl() {
        return defaultUrl;
    }

    public static Set<String> getSiteTreeSet() {
        return siteTreeSet;
    }

    public MyRunnable() {
        recursiveParser = new RecursiveParser(RecursiveParser.getUrlsMap());
        try {
            new ForkJoinPool().invoke(new RecursiveParser(PageParser.parsing(MyRunnable.getDefaultUrl())));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        try {
            FileWriter fileWriter = new FileWriter("data/siteMap.txt");
            fileWriter.write(PageParser.getStringBuilder().toString());
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        recursiveParser.compute();

    }
}
