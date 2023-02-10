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
        List<RecursiveParser> taskList = new ArrayList<>();
        Set<String> urls = new HashSet<>();
        try {
            for (String url : urlsMap) {
                RecursiveParser task = new RecursiveParser(pageParser.parsing(url), pageParser);
                task.fork();
                taskList.add(task);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (RecursiveParser task : taskList) {
            urls.addAll(task.join());
        }
        return urls;
    }
}
