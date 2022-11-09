import java.util.ArrayList;
import java.util.List;

public class StarterParsing {

    private static String url1 = "https://skillbox.ru/";
    private static String url2 = "http://www.playback.ru/";
    private static String url3 = "https://volochek.life/";

    public static void main(String[] args) {

        List<String> urls = new ArrayList<>();
        urls.add(url1);
        urls.add(url2);
        urls.add(url3);

        for (String url : urls) {
            new Thread(new MyRunnable(new Site(url))).start();
        }
    }
}
