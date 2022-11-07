import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;

public class StarterParsing {

    private static String defaultUrl = "https://skillbox.ru/";

    private static Set<String> siteTreeSet = new LinkedHashSet<>();

    public static String getDefaultUrl() {
        return defaultUrl;
    }

    public static Set<String> getSiteTreeSet() {
        return siteTreeSet;
    }

    public static void main(String[] args) throws InterruptedException {
        try {
            FileWriter fileWriter = new FileWriter("data/siteMap.txt");
            new ForkJoinPool().invoke(new RecursiveParser(PageParser.parsing(defaultUrl)));
            fileWriter.write(PageParser.getStringBuilder().toString());
            fileWriter.flush();
            fileWriter.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
