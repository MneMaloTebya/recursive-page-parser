import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.RecursiveTask;

public class RecursiveParser extends RecursiveTask<Set<String>> {

    private Set<String> urlsMap;
    private PageParser pageParser;

    public RecursiveParser(Set<String> urlsMap, PageParser pageParser) {
        this.urlsMap = urlsMap;
        this.pageParser = pageParser;
    }

    @Override
    protected Set<String> compute() {
        Set<String> urls = new HashSet<>();
        try {

            List<RecursiveParser> tasks = new ArrayList<>();
            for (String url : urlsMap) {
                RecursiveParser task = new RecursiveParser(pageParser.parsing(url), pageParser);
                task.fork();
                tasks.add(task);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return urls;
    }
}
