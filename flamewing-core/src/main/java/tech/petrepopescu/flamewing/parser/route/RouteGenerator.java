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
            RequestMapping classAnnotation = controllerClass.getAnnotation(RequestMapping.class);
            String basePath = "";
            if (classAnnotation != null) {
                if (classAnnotation.value().length > 0) {
                    basePath = classAnnotation.value()[0];
                } else if (classAnnotation.path().length > 0) {
                    basePath = classAnnotation.path()[0];
                }
            }

            List<RoutePath> paths = new ArrayList<>();
            for (Method m:controllerClass.getMethods()) {
                GetMapping getAnnotation = m.getAnnotation(GetMapping.class);
                if (getAnnotation != null) {
                    String methodPath = "";
                    if (getAnnotation.value().length > 0) {
                        methodPath = getAnnotation.value()[0];
                    } else if (getAnnotation.path().length > 0) {
                        methodPath = getAnnotation.path()[0];
                    }
                    paths.add(parseAnnotation(combinePaths(basePath, methodPath), m, HttpMethod.GET));
                }

                PostMapping postAnnotation = m.getAnnotation(PostMapping.class);
                if (postAnnotation != null) {
                    String methodPath = "";
                    if (postAnnotation.value().length > 0) {
                        methodPath = postAnnotation.value()[0];
                    } else if (postAnnotation.path().length > 0) {
                        methodPath = postAnnotation.path()[0];
                    }
                    paths.add(parseAnnotation(combinePaths(basePath, methodPath), m, HttpMethod.POST));
                }

                PutMapping putAnnotation = m.getAnnotation(PutMapping.class);
                if (putAnnotation != null) {
                    String methodPath = "";
                    if (putAnnotation.value().length > 0) {
                        methodPath = putAnnotation.value()[0];
                    } else if (putAnnotation.path().length > 0) {
                        methodPath = putAnnotation.path()[0];
                    }
                    paths.add(parseAnnotation(combinePaths(basePath, methodPath), m, HttpMethod.PUT));
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
        String configuredPackage = config.getControllersPackage();

        if (StringUtils.isBlank(configuredPackage) || "com".equals(configuredPackage)) {
            return "routes";
        }

        if (packageName.equals(configuredPackage)) {
            return "routes";
        }

        if (packageName.startsWith(configuredPackage + ".")) {
            return "routes." + packageName.substring(configuredPackage.length() + 1);
        }

        return "routes";
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
                if (StringUtils.isBlank(varName)) {
                    varName = param.getName();
                }
                pathVariables.add(
                        RouteVariable.builder()
                                .name(varName)
                                .varName(varName)
                                .varType(param.getType())
                                .required(pathVariable.required())
                                .build()

                );
            }

            RequestParam requestParam = param.getAnnotation(RequestParam.class);
            if (requestParam != null) {
                String varName = requestParam.name();
                if (StringUtils.isBlank(varName)) {
                    varName = requestParam.value();
                }
                if (StringUtils.isBlank(varName)) {
                    varName = param.getName();
                }
                requestVariables.add(
                        RouteVariable.builder()
                                .name(varName)
                                .varName(varName)
                                .varType(param.getType())
                                .required(requestParam.required())
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

    String getFileContent(String name, String classPackage, List<RoutePath> paths) {
        StringBuilder builder = new StringBuilder();
        builder.append("package ").append(classPackage).append(";\n\n");
        builder.append("import org.springframework.web.servlet.support.ServletUriComponentsBuilder;\n");
        builder.append("import org.springframework.http.HttpMethod;\n");
        builder.append("import tech.petrepopescu.flamewing.route.Route;\n\n");
        builder.append("public final class " + name + " {\n\n");
        builder.append("\tprivate static final String BASE_ROUTE = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();\n\n");

        for(RoutePath path:paths) {
            boolean hasOptional = path.getRequestVariables().stream().anyMatch(v -> !v.isRequired()) ||
                    path.getPathVariables().stream().anyMatch(v -> !v.isRequired());

            if (hasOptional) {
                builder.append("\tpublic static Route " + getMethodSignature(path, true) + " {\n");
                builder.append("\t\treturn Route.of(BASE_ROUTE + \"" + getUrl(path, true) + "\", HttpMethod." + path.getMethod() + ");\n");
                builder.append("\t}\n\n");
            }

            builder.append("\tpublic static Route " + getMethodSignature(path, false) + " {\n");
            builder.append("\t\treturn Route.of(BASE_ROUTE + \"" + getUrl(path, false) + "\", HttpMethod." + path.getMethod() + ");\n");
            builder.append("\t}\n");
        }


        builder.append("}\n");

        return builder.toString();
    }

    String getMethodSignature(RoutePath path, boolean mandatoryOnly) {
        StringBuilder varBuilder = new StringBuilder();
        for (RouteVariable pathVar:path.getPathVariables()) {
            if (mandatoryOnly && !pathVar.isRequired()) {
                continue;
            }
            if (!StringUtils.isEmpty(varBuilder)) {
                varBuilder.append(", ");
            }
            varBuilder.append(pathVar.getVarType().getName()).append(" ").append(pathVar.getVarName());
        }

        for (RouteVariable pathVar:path.getRequestVariables()) {
            if (mandatoryOnly && !pathVar.isRequired()) {
                continue;
            }
            if (!StringUtils.isEmpty(varBuilder)) {
                varBuilder.append(", ");
            }
            varBuilder.append(pathVar.getVarType().getName()).append(" ").append(pathVar.getVarName());
        }

        return path.getName() + "(" + varBuilder + ")";
    }


    String getUrl(RoutePath path, boolean mandatoryOnly) {
        String fullUrl = path.getUrl();

        for (RouteVariable pathVar:path.getPathVariables()) {
            if (mandatoryOnly && !pathVar.isRequired()) {
                continue;
            }
            fullUrl = StringUtils.replace(fullUrl, "{" + pathVar.getName() + "}", "\" + " + pathVar.getVarName() + " + \"");
        }

        StringBuilder requestArgsBuilder = new StringBuilder();
        for (RouteVariable pathVar:path.getRequestVariables()) {
            if (mandatoryOnly && !pathVar.isRequired()) {
                continue;
            }
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

    String combinePaths(String base, String methodPath) {
        if (StringUtils.isBlank(base)) {
            return methodPath;
        }
        if (StringUtils.isBlank(methodPath)) {
            return base;
        }

        String result = base;
        if (!result.startsWith("/")) {
            result = "/" + result;
        }
        if (result.endsWith("/")) {
            result = result.substring(0, result.length() - 1);
        }

        String mPath = methodPath;
        if (!mPath.startsWith("/")) {
            mPath = "/" + mPath;
        }

        return result + mPath;
    }


}
