package tech.petrepopescu.flamewing.idea;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class FlamewingFileType extends LanguageFileType {
    public static final FlamewingFileType INSTANCE = new FlamewingFileType();

    private FlamewingFileType() {
        super(FlamewingLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {
        return "Flamewing File";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Flamewing template file";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "java.html";
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return null; // To be implemented
    }
}
