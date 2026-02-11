package tech.petrepopescu.flamewing.spring.config;

public class ViewsConfiguration {
    private String path = "views";
    private String extension = ".java.html";

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
}
