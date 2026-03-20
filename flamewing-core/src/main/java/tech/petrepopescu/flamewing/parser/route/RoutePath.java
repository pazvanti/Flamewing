package tech.petrepopescu.flamewing.parser.route;

import org.springframework.http.HttpMethod;

import java.util.List;

public class RoutePath {
    private String url;
    private String name;
    private HttpMethod method;
    private List<RouteVariable> pathVariables;
    private List<RouteVariable> requestVariables;

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public List<RouteVariable> getPathVariables() {
        return pathVariables;
    }

    public List<RouteVariable> getRequestVariables() {
        return requestVariables;
    }

    public static RoutePathBuilder builder() {
        return new RoutePathBuilder();
    }

    static class RoutePathBuilder {
        private String url;
        private String name;
        private HttpMethod method;
        private List<RouteVariable> pathVariables;
        private List<RouteVariable> requestVariables;

        public RoutePathBuilder url(String url) {
            this.url = url;
            return this;
        }

        public RoutePathBuilder name(String name) {
            this.name = name;
            return this;
        }

        public RoutePathBuilder method(HttpMethod method) {
            this.method = method;
            return this;
        }

        public RoutePathBuilder pathVariables(List<RouteVariable> pathVariables) {
            this.pathVariables = pathVariables;
            return this;
        }

        public RoutePathBuilder requestVariables(List<RouteVariable> requestVariables) {
            this.requestVariables = requestVariables;
            return this;
        }

        public RoutePath build() {
            RoutePath routePath = new RoutePath();
            routePath.url = this.url;
            routePath.name = this.name;
            routePath.method = this.method;
            routePath.pathVariables = this.pathVariables;
            routePath.requestVariables = this.requestVariables;

            return routePath;
        }
    }
}
