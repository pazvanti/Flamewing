package tech.petrepopescu.flamewing.gradle;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.TaskProvider;

import java.io.File;

public class FlamewingPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        FlamewingExtension extension = project.getExtensions().create("flamewing", FlamewingExtension.class);
        
        File generatedSourceDir = new File(project.getBuildDir(), "generated/sources/flamewing/java/main");
        
        TaskProvider<GenerateFlamewingSourcesTask> generateTask = project.getTasks().register("generateFlamewingSources", GenerateFlamewingSourcesTask.class, task -> {
            task.getOutputDirectory().set(generatedSourceDir);
            
            task.getViewsDirectory().fileProvider(
                extension.getViews().getPath().map(path -> project.file(path))
            ).convention(project.getLayout().getProjectDirectory().dir("src/main/resources/views"));
            
            task.getViewsExtension().set(extension.getViews().getExtension());
            task.getControllersPackage().set(extension.getControllersPackage());
        });

        project.getPlugins().withType(org.gradle.api.plugins.JavaPlugin.class, javaPlugin -> {
            JavaPluginExtension javaExtension = project.getExtensions().getByType(JavaPluginExtension.class);
            SourceSet mainSourceSet = javaExtension.getSourceSets().getByName(SourceSet.MAIN_SOURCE_SET_NAME);
            
            generateTask.configure(task -> {
                task.getClassesDirectories().from(project.getTasks().named(mainSourceSet.getCompileJavaTaskName()));
                task.getClassesDirectories().from(mainSourceSet.getCompileClasspath());
            });

            TaskProvider<org.gradle.api.tasks.compile.JavaCompile> compileFlamewing = project.getTasks().register("compileFlamewing", org.gradle.api.tasks.compile.JavaCompile.class, task -> {
                task.source(generatedSourceDir);
                task.setClasspath(mainSourceSet.getCompileClasspath().plus(project.files(project.getTasks().named(mainSourceSet.getCompileJavaTaskName()))));
                task.getDestinationDirectory().set(new File(project.getBuildDir(), "classes/java/flamewing"));
                task.dependsOn(generateTask);
            });

            mainSourceSet.getOutput().dir(java.util.Collections.singletonMap("builtBy", compileFlamewing), new File(project.getBuildDir(), "classes/java/flamewing"));
        });
    }
}
