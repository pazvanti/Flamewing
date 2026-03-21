package tech.petrepopescu.flamewing.idea;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import org.jetbrains.annotations.NotNull;

public class FlamewingStartupActivity implements StartupActivity {
    @Override
    public void runActivity(@NotNull Project project) {
        ApplicationManager.getApplication().invokeLater(() -> {
            ApplicationManager.getApplication().runWriteAction(() -> {
                FileTypeManager.getInstance().associatePattern(FlamewingFileType.INSTANCE, "*.java.html");
            });
        });
    }
}
