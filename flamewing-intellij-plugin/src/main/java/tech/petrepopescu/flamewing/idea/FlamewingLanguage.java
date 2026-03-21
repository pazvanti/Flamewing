package tech.petrepopescu.flamewing.idea;

import com.intellij.lang.Language;

public class FlamewingLanguage extends Language {
    public static final FlamewingLanguage INSTANCE = new FlamewingLanguage();

    private FlamewingLanguage() {
        super("Flamewing");
    }
}
