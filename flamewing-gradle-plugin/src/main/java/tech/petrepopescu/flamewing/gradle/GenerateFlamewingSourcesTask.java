package tech.petrepopescu.flamewing.gradle;

import org.gradle.api.DefaultTask;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.*;
import tech.petrepopescu.flamewing.parser.ElementFactory;
import tech.petrepopescu.flamewing.parser.FlamewingParser;
import tech.petrepopescu.flamewing.parser.TemplateFile;
import tech.petrepopescu.flamewing.parser.VariableRegistry;
import tech.petrepopescu.flamewing.parser.compiler.FlamewingCompilerImpl;
import tech.petrepopescu.flamewing.parser.compiler.DynamicClassLoader;
import tech.petrepopescu.flamewing.parser.compiler.SourceCodeObject;
import tech.petrepopescu.flamewing.parser.route.RouteGenerator;
import tech.petrepopescu.flamewing.special.FlamewingSpecialElementsUtil;
import tech.petrepopescu.flamewing.spring.config.FlamewingConfiguration;

import javax.tools.JavaFileObject;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public abstract class GenerateFlamewingSourcesTask extends DefaultTask {

    @OutputDirectory
    public abstract DirectoryProperty getOutputDirectory();

    @InputDirectory
    public abstract DirectoryProperty getViewsDirectory();

    @Input
    @Optional
    public abstract Property<String> getControllersPackage();

    @TaskAction
    public void generate() throws IOException {
        File outputDir = getOutputDirectory().get().getAsFile();
        if (outputDir.exists()) {
            deleteDirectory(outputDir);
        }
        outputDir.mkdirs();

        VariableRegistry.getInstance().reset();

        FlamewingConfiguration config = new FlamewingConfiguration();
        config.getViews().setPath(getViewsDirectory().get().getAsFile().getAbsolutePath());
        if (getControllersPackage().isPresent()) {
            config.setControllersPackage(getControllersPackage().get());
        }

        // We don't need a real compiler or class loader for generation
        FlamewingCompilerImpl mockCompiler = new FlamewingCompilerImpl(new DynamicClassLoader(Thread.currentThread().getContextClassLoader()));
        ElementFactory elementFactory = new ElementFactory(null);
        RouteGenerator routeGenerator = new RouteGenerator(config);
        
        FlamewingParser parser = new FlamewingParser(elementFactory, routeGenerator, mockCompiler, config);
        
        List<JavaFileObject> sources = parser.generateAllSources();
        
        for (JavaFileObject source : sources) {
            if (source instanceof SourceCodeObject s) {
                File packageDir = new File(outputDir, s.getBasePackage().replace(".", "/"));
                packageDir.mkdirs();
                File outputFile = new File(packageDir, s.getName() + ".java");
                Files.writeString(outputFile.toPath(), s.getContent());
            }
        }
    }

    private void deleteDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        directory.delete();
    }
}
