package tech.petrepopescu.flamewing.parser.route;

import tech.petrepopescu.flamewing.spring.config.FlamewingConfiguration;
import tech.petrepopescu.flamewing.utils.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import tech.petrepopescu.flamewing.parser.compiler.SourceCodeObject;

import javax.tools.JavaFileObject;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class RouteGenerator {
    private final FlamewingConfiguration config;
    private final AnnotationScanner annotationScanner = new AnnotationScanner();

    public RouteGenerator(FlamewingConfiguration config) {
        this.config = config;
    }

    public List<JavaFileObject> generateRoutes() {
        List<JavaFileObject> fileObjects = new ArrayList<>();
        Set<Class<?>> allControllers = annotationScanner.getTypesAnnotatedWith(Controller.class);

        for (Class<?> controllerClass:allControllers) {
            List<RoutePath> paths = new ArrayList<>();
            for (Method m:controllerClass.getMethods()) {
                GetMapping getAnnotation = m.getAnnotation(GetMapping.class);
                if (getAnnotation != null) {
                    if (getAnnotation.value().length > 0) {
                        paths.add(parseAnnotation(getAnnotation.value()[0], m, HttpMethod.GET));
                    } else if (getAnnotation.path().length > 0) {
                        paths.add(parseAnnotation(getAnnotation.path()[0], m, HttpMethod.GET));
                    }
                }

                PostMapping postAnnotation = m.getAnnotation(PostMapping.class);
                if (postAnnotation != null) {
                    if (postAnnotation.value().length > 0) {
                        paths.add(parseAnnotation(postAnnotation.value()[0], m, HttpMethod.POST));
                    } else if (postAnnotation.path().length > 0) {
                        paths.add(parseAnnotation(postAnnotation.path()[0], m, HttpMethod.POST));
                    }
                }

                PutMapping putAnnotation = m.getAnnotation(PutMapping.class);
                if (putAnnotation != null) {
                    if (putAnnotation.value().length > 0) {
                        paths.add(parseAnnotation(putAnnotation.value()[0], m, HttpMethod.PUT));
                    } else if (putAnnotation.path().length > 0) {
                        paths.add(parseAnnotation(putAnnotation.path()[0], m, HttpMethod.PUT));
                    }
                }
            }

            String controllerClassName = controllerClass.getSimpleName();
            String controllerBasePackage = extractPackage(controllerClass);
            String content = getFileContent(controllerClass.getSimpleName(), controllerBasePackage, paths);
            JavaFileObject fileObject = new SourceCodeObject(controllerClassName, content, controllerBasePackage);
            fileObjects.add(fileObject);
        }

        return fileObjects;
    }

    private String extractPackage(Class<?> controllerClass) {
        String packageName = controllerClass.getPackageName();
        if (packageName.equals(config.getControllersPackage())) {
            return "routes";
        }

        return "routes." + StringUtils.replace(controllerClass.getPackageName(), config.getControllersPackage() + ".", "");
    }

    private RoutePath parseAnnotation(String url, Method method, HttpMethod httpMethod) {
        String name = method.getName();

        Parameter[] parameters = method.getParameters();
        List<RouteVariable> pathVariables = new ArrayList<>();
        List<RouteVariable> requestVariables = new ArrayList<>();
        for (Parameter param:parameters) {
            PathVariable pathVariable = param.getAnnotation(PathVariable.class);
            if (pathVariable != null) {
                String varName = pathVariable.name();
                if (StringUtils.isBlank(varName)) {
                    varName = pathVariable.value();
                }
                pathVariables.add(
                        RouteVariable.builder()
                                .name(varName)
                                .varName(varName)
                                .varType(param.getType())
                                .build()
                );
            }

            RequestParam requestParam = param.getAnnotation(RequestParam.class);
            if (requestParam != null) {
                String varName = requestParam.name();
                if (StringUtils.isBlank(varName)) {
                    varName = requestParam.value();
                }
                requestVariables.add(
                        RouteVariable.builder()
                                .name(varName)
                                .varName(varName)
                                .varType(param.getType())
                                .build()
                );
            }
        }

        if (!StringUtils.startsWith(url, "/")) {
            url = "/" + url;
        }
        return RoutePath.builder().url(url)
                .name(name)
                .pathVariables(pathVariables)
                .requestVariables(requestVariables)
                .method(httpMethod)
                .build();
    }

    private String getFileContent(String name, String classPackage, List<RoutePath> paths) {
        StringBuilder builder = new StringBuilder();
        builder.append("package ").append(classPackage).append(";\n\n");
        builder.append("import org.springframework.web.servlet.support.ServletUriComponentsBuilder;\n");
        builder.append("import org.springframework.http.HttpMethod;\n");
        builder.append("import tech.petrepopescu.flamewing.route.Route;\n\n");
        builder.append("public final class " + name + " {\n\n");
        builder.append("\tprivate static final String BASE_ROUTE = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();\n\n");

        for(RoutePath path:paths) {
            builder.append("\tpublic static Route " + getMethodSignature(path) + " {\n");
            builder.append("\t\treturn Route.of(BASE_ROUTE + \"" + getUrl(path) + "\", HttpMethod." + path.getMethod() + ");\n");
            builder.append("\t}\n");
        }

        builder.append("}\n");

        return builder.toString();
    }

    private String getMethodSignature(RoutePath path) {
        StringBuilder varBuilder = new StringBuilder();
        for (RouteVariable pathVar:path.getPathVariables()) {
            if (!StringUtils.isEmpty(varBuilder)) {
                varBuilder.append(", ");
            }
            varBuilder.append(pathVar.getVarType().getName()).append(" ").append(pathVar.getVarName());
        }

        for (RouteVariable pathVar:path.getRequestVariables()) {
            if (!StringUtils.isEmpty(varBuilder)) {
                varBuilder.append(", ");
            }
            varBuilder.append(pathVar.getVarType().getName()).append(" ").append(pathVar.getVarName());
        }

        return path.getName() + "(" + varBuilder + ")";
    }

    private String getUrl(RoutePath path) {
        String fullUrl = path.getUrl();

        for (RouteVariable pathVar:path.getPathVariables()) {
            fullUrl = StringUtils.replace(fullUrl, "{" + pathVar.getName() + "}", "\" + " + pathVar.getVarName() + " + \"");
        }

        StringBuilder requestArgsBuilder = new StringBuilder();
        for (RouteVariable pathVar:path.getRequestVariables()) {
            if (!StringUtils.isEmpty(requestArgsBuilder)) {
                requestArgsBuilder.append("&");
            }

            requestArgsBuilder.append(pathVar.getName()).append("=\" + ").append(pathVar.getVarName()).append(" + \"");
        }

        if (StringUtils.isBlank(requestArgsBuilder)) {
            return fullUrl;
        }

        return fullUrl + "?" + requestArgsBuilder;
    }
}
