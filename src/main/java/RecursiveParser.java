import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.RecursiveTask;

public class RecursiveParser extends RecursiveTask<Set<String>> {
    private Set<String> urlsMap;

    public RecursiveParser(Set<String> urlsMap) {
        this.urlsMap = urlsMap;
    }

    @Override
    protected Set<String> compute() {
        Set<String> urls = new HashSet<>();
        try {

            List<RecursiveParser> tasks = new ArrayList<>();
            for (String url : urlsMap) {
                RecursiveParser task = new RecursiveParser(PageParser.parsing(url));
                task.fork();
                tasks.add(task);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return urls;
    }

}
