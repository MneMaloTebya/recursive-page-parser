import java.util.ArrayList;
import java.util.List;

public class StarterParsing {

    public static void main(String[] args) {
        String url1 = "https://skillbox.ru/";
        String url2 = "http://www.playback.ru/";
        String url3 = "https://volochek.life/";

        List<String> urls = new ArrayList<>();

        urls.add(url1);
        urls.add(url2);
        urls.add(url3);

        for (String url : urls) {
            Thread thread1 = new Thread(new MyRunnableParser(url));
            thread1.start();
        }
    }
}
