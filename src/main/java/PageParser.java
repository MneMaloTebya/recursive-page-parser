import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

public class PageParser {

    private final List<String> STOP_WORDS = Arrays
            .asList("vk", "pdf", "twitter", "facebook", "instagram", "utm", "JPG",
                    "jpg", "jpeg", "JPEG", "png", "hh", "youtube", "apple", "yandex",
                    "google", "webp", "zip", "wa.me", "+", ",", "docx");

    private StringBuilder stringBuilder = new StringBuilder();
    private final Object object = new Object();
    private Site site;

    public PageParser(Site site) {
        this.site = site;
    }

    public Set<String> parsing(String currentUrl) throws InterruptedException {
        Thread.sleep(500);
        Set<String> urlSet = new HashSet<>();
        try {
            var response = getResponse(currentUrl);
            Document document = response.parse();
            Elements elements = document.select("a");
            for (Element element : elements) {
                String url = element.attr("href");
                boolean condition1 = url.startsWith("/");
                boolean condition2 = (url.contains(document.location()));
                boolean condition3 = STOP_WORDS.stream().noneMatch(url::contains);
                if (condition1 && condition3) {
                    url = site.getFullName() + url.substring(1);
                    addUrl(urlSet, url);
                }
                if (condition2 && condition3) {
                    addUrl(urlSet, url);
                }
            }
        } catch (HttpStatusException e) {
            return Collections.EMPTY_SET;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return urlSet;
    }

    private void addUrl(Set<String> urlSet, String url) {
        synchronized (object) {
            if (!site.getUrlSiteSet().contains(url)) {
                urlSet.add(url);
                site.getUrlSiteSet().add(url);
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

    public StringBuilder getStringBuilder() {
        return stringBuilder;
    }
}
