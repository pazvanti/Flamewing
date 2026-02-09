package tech.petrepopescu.flamewing.parser.compiler;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import tech.petrepopescu.flamewing.exception.CompilationException;
import tech.petrepopescu.flamewing.parser.ByteCodeGeneratingFileManager;
import tech.petrepopescu.flamewing.parser.route.ByteCodeObject;
import tech.petrepopescu.flamewing.views.View;

import javax.tools.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class Compiler extends FlamewingCompiler {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(Compiler.class);

    private final JavaCompiler javaCompiler;

    private final DynamicClassLoader dynamicClassLoader;
    private final DiagnosticCollector<JavaFileObject> diagnostics;
    private final ByteCodeGeneratingFileManager fileManager;
    private final List<String> loadedClasses = new ArrayList<>();

    public Compiler(DynamicClassLoader dynamicClassLoader) {
        this.dynamicClassLoader = dynamicClassLoader;
        this.javaCompiler = ToolProvider.getSystemJavaCompiler();
        this.diagnostics = new DiagnosticCollector<>();
        this.fileManager = new ByteCodeGeneratingFileManager(javaCompiler.getStandardFileManager(diagnostics, null, null));
    }

    public void compileAndLoad(List<JavaFileObject> javaFileObjects) {
        compile(javaFileObjects);
        for (JavaFileObject fileObject:javaFileObjects) {
            if (fileObject instanceof SourceCodeObject source) {
                byte[] bytecode = this.fileManager.getBytecode(source.getFullClassName());
                Class<?> resultedClass = load(source.getFullClassName(), bytecode);
                if (source.isTemplateFile()){
                    View.add(resultedClass, source.getFullClassName());
                }
            }
        }

        // Load classes that were not loaded
        Map<String, ByteCodeObject> byteCodeObjectMap = this.fileManager.getBytecodeObjects();
        for (Map.Entry<String, ByteCodeObject> byteCodeObjectEntry:byteCodeObjectMap.entrySet()) {
            if (!loadedClasses.contains(byteCodeObjectEntry.getKey())) {
                load(byteCodeObjectEntry.getKey(), byteCodeObjectEntry.getValue().getBytecode().toByteArray());
            }
        }
    }

    protected void compile(List<JavaFileObject> javaFileObjects) {
        if (CollectionUtils.isEmpty(javaFileObjects)) {
            log.warn("Nothing to compile");
            return;
        }

        // Compile the code
        JavaCompiler.CompilationTask task = javaCompiler.getTask(null, fileManager, diagnostics, null, null, javaFileObjects);
        Boolean result = task.call();
        if (result == null || !result) {
            List<Diagnostic<? extends JavaFileObject>> diagnosticList = diagnostics.getDiagnostics();
            for (Diagnostic<? extends JavaFileObject> diagnostic:diagnosticList) {
                log.error(diagnostic.toString());
            }
            throw new CompilationException("Compilation failed.");
        }
    }

    public Class<?> load(String className, byte[] bytecode) {
        loadedClasses.add(className);
        return dynamicClassLoader.loadClass(className, bytecode);
    }
}
