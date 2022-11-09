import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

public class PageParser {

    private static final List<String> STOP_WORDS = Arrays
            .asList("vk", "pdf", "twitter", "facebook", "instagram", "utm", "JPG",
                    "jpg", "jpeg", "JPEG", "png", "hh", "youtube", "apple", "yandex", "google");

    private static StringBuilder stringBuilder = new StringBuilder();

    public static StringBuilder getStringBuilder() {
        return stringBuilder;
    }

    public static Set<String> parsing(String currentUrl) throws InterruptedException {
        Thread.sleep(150);
        Set<String> urlSet = new HashSet<>();
        try {
            Document document = getResponse(currentUrl).parse();
            Elements elements = document.select("a");
            for (Element element : elements) {
                String url = element.attr("href");

                boolean condition1 = url.startsWith("/");
                boolean condition2 = (url.startsWith("http") || (url.startsWith("https")));
                boolean condition3 = STOP_WORDS.stream().noneMatch(url::contains);

                if (condition1 && condition3) {
                    url = Site.getName() + url.substring(1);
                    addUrl(url, urlSet);
                } else if (condition2 && condition3) {
                    addUrl(url, urlSet);
                }
            }
        } catch (HttpStatusException e) {
            return Collections.EMPTY_SET;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return urlSet;
    }

    private static void addUrl(String url, Set<String> urlSet) {
        synchronized (Site.getSiteTreeSet()) {
            if (!Site.getSiteTreeSet().contains(url)) {
                urlSet.add(url);
                Site.getSiteTreeSet().add(url);
                stringBuilder.append(url + "\n");
            }
        }
    }

    private static Connection.Response getResponse(String linkPage) throws IOException {
        Connection.Response response = Jsoup.connect(linkPage)
                .userAgent("Mozilla/5.0 (Windows; U; WindowsNT5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                .referrer("http://google.com")
                .timeout(0)
                .execute();
        return response;
    }
}
