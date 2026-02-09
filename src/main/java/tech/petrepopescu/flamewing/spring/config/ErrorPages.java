package tech.petrepopescu.flamewing.spring.config;

public class ErrorPages {
    private String code404 = null;
    private String code500 = null;

    public String getCode404() {
        return code404;
    }

    public void setCode404(String code404) {
        this.code404 = code404;
    }

    public String getCode500() {
        return code500;
    }

    public void setCode500(String code500) {
        this.code500 = code500;
    }
}
