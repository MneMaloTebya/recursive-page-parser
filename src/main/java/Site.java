import java.util.LinkedHashSet;
import java.util.Set;

public class Site {

    private static String name;

    private static Set<String> siteTreeSet;

    public Site(String name) {
        siteTreeSet = new LinkedHashSet<>();
        Site.name = name;
    }

    public static String cutPath() {
        return name
                .replace("https", "")
                .replace("http", "")
                .replace("://", "")
                .replace(".", "")
                .replace("/", "");
    }

    public static Set<String> getSiteTreeSet() {
        return siteTreeSet;
    }

    public static String getName() {
        return name;
    }
}
