import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PageParser {

    private static final List<String> STOP_WORDS = Arrays
            .asList("vk", "pdf", "twitter", "facebook", "instagram", "utm", "JPG",
                    "jpg", "jpeg", "JPEG", "png", "hh", "youtube", "apple", "yandex", "google");

    private final static Object obj = new Object();
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
                boolean condition2 =
                        (url.startsWith("http") || (url.startsWith("https")))
                                && url.contains(getDesiredGroupOfURL(url, 4));
                boolean condition3 = STOP_WORDS.stream().noneMatch(url::contains);

                if (condition1 && condition3) {
                    url = StarterParsing.getDefaultUrl() + url.substring(1);
                    if (STOP_WORDS.stream().noneMatch(url::contains)) {
                        addUrl(url, urlSet);
                    }
                } else if (condition2 && condition3) {
                    if (STOP_WORDS.stream().noneMatch(url::contains)) {
                        addUrl(url, urlSet);
                    }
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
        synchronized (obj) {
            if (!StarterParsing.getSiteTreeSet().contains(url)) {
                urlSet.add(url);
                StarterParsing.getSiteTreeSet().add(url);
                stringBuilder.append(url + "\n");
            }
        }
    }

    private static String getDesiredGroupOfURL(String url, int group) {
        Pattern pattern = Pattern.compile("^(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\\?([^#]*))?(#(.*))?");
        String desiredGroup = null;
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            switch (group) {
                case 1:
                    desiredGroup = matcher.group(1);
                    break;
                case 2:
                    desiredGroup = matcher.group(2);
                    break;
                case 3:
                    desiredGroup = matcher.group(3);
                    break;
                case 4:
                    desiredGroup = matcher.group(4);
                    break;
                case 5:
                    desiredGroup = matcher.group(5);
                    break;
            }
        }
        return desiredGroup;
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
