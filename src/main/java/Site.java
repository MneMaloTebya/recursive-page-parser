import java.util.LinkedHashSet;
import java.util.Set;

public class Site {

    private String fullName;

    private Set<String> urlSiteSet = new LinkedHashSet<>();

    public Site(String fullName) {
        this.fullName = fullName;
    }

    public String cutDomain() {
        return fullName
                .replace("https", "")
                .replace("http", "")
                .replace("://", "")
                .replace(".", "")
                .replace("/", "");
    }

    public Set<String> getUrlSiteSet() {
        return urlSiteSet;
    }

    public String getFullName() {
        return fullName;
    }
}
