package tech.petrepopescu.flamewing.route;

import org.springframework.http.HttpMethod;

public class Route {
    private final String path;
    private final HttpMethod method;

    public Route(String path, HttpMethod method) {
        this.path = path;
        this.method = method;
    }

    public static Route of(String path, HttpMethod method) {
        return new Route(path, method);
    }

    public String path() {
        return path;
    }

    public String method() {
        return method.name();
    }
}
