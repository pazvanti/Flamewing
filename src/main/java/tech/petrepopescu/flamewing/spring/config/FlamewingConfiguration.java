package tech.petrepopescu.flamewing.spring.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "flamewing")
public class FlamewingConfiguration {
    private String controllersPackage = "com";
    private ViewsConfiguration views = new ViewsConfiguration();
    private ErrorPages errorPages;
    private boolean fragmentRetrieveEnabled = false;

    public String getControllersPackage() {
        return controllersPackage;
    }

    public void setControllersPackage(String controllersPackage) {
        this.controllersPackage = controllersPackage;
    }

    public ErrorPages getErrorPages() {
        return errorPages;
    }

    public void setErrorPages(ErrorPages errorPages) {
        this.errorPages = errorPages;
    }

    public boolean isFragmentRetrieveEnabled() {
        return fragmentRetrieveEnabled;
    }

    public void setFragmentRetrieveEnabled(boolean fragmentRetrieveEnabled) {
        this.fragmentRetrieveEnabled = fragmentRetrieveEnabled;
    }

    public ViewsConfiguration getViews() {
        return views;
    }

    public void setViews(ViewsConfiguration views) {
        this.views = views;
    }
}
