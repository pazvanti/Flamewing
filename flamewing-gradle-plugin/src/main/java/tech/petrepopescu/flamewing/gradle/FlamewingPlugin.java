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
        File generatedSourceDir = new File(project.getBuildDir(), "generated/sources/flamewing/java/main");
        
        TaskProvider<GenerateFlamewingSourcesTask> generateTask = project.getTasks().register("generateFlamewingSources", GenerateFlamewingSourcesTask.class, task -> {
            task.getOutputDirectory().set(generatedSourceDir);
            task.getViewsDirectory().set(new File(project.getProjectDir(), "src/main/resources/views"));
        });

        project.getPlugins().withType(org.gradle.api.plugins.JavaPlugin.class, javaPlugin -> {
            JavaPluginExtension javaExtension = project.getExtensions().getByType(JavaPluginExtension.class);
            SourceSet mainSourceSet = javaExtension.getSourceSets().getByName(SourceSet.MAIN_SOURCE_SET_NAME);
            mainSourceSet.getJava().srcDir(generatedSourceDir);
            
            project.getTasks().named("compileJava").configure(compileJava -> {
                compileJava.dependsOn(generateTask);
            });
        });
    }
}
