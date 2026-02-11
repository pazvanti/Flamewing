package tech.petrepopescu.flamewing.parser.route;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

public class AnnotationScanner {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AnnotationScanner.class);

    public Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation> annotation) {
        Set<Class<?>> annotatedClasses = new HashSet<>();
        String packageName = ""; // Scan all packages - you can specify a package to limit scope

        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            String path = packageName.replace('.', '/');
            Enumeration<URL> resources = classLoader.getResources(path);

            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                String protocol = resource.getProtocol();

                if ("file".equals(protocol)) {
                    // Only scan file system directories (source classes)
                    String filePath = URLDecoder.decode(resource.getFile(), StandardCharsets.UTF_8);
                    scanDirectory(new File(filePath), packageName, annotation, annotatedClasses);
                }
                // Skip jar files - only scan current sources
            }
        } catch (Exception e) {
            throw new RuntimeException("Error scanning for annotated classes", e);
        }

        return annotatedClasses;
    }

    private void scanDirectory(File directory, String packageName,
                               Class<? extends Annotation> annotation,
                               Set<Class<?>> annotatedClasses) {
        if (!directory.exists()) {
            return;
        }

        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                String subPackage = packageName.isEmpty() ? file.getName() : packageName + "." + file.getName();
                scanDirectory(file, subPackage, annotation, annotatedClasses);
            } else if (file.getName().endsWith(".class")) {
                String className = getClassName(file.getName(), packageName);
                checkClassForAnnotation(className, annotation, annotatedClasses);
            }
        }
    }

    private String getClassName(String fileName, String packageName) {
        String className = fileName.substring(0, fileName.length() - 6); // Remove .class extension
        return packageName.isEmpty() ? className : packageName + "." + className;
    }

    private void checkClassForAnnotation(String className, Class<? extends Annotation> annotation,
                                         Set<Class<?>> annotatedClasses) {
        try {
            Class<?> clazz = Class.forName(className);

            if (clazz.isAnnotationPresent(annotation)) {
                annotatedClasses.add(clazz);
            }
        } catch (ClassNotFoundException | NoClassDefFoundError | ExceptionInInitializerError e) {
            // Skip classes that can't be loaded - common in classpath scanning
        } catch (Exception e) {
            log.error("Warning: Could not examine class " + className + ": " + e.getMessage(), e);
        }
    }
}
