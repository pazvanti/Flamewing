package tech.petrepopescu.flamewing.idea;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class FlamewingTokenType extends IElementType {
    public FlamewingTokenType(@NotNull @NonNls String debugName) {
        super(debugName, FlamewingLanguage.INSTANCE);
    }

    @Override
    public String toString() {
        return "FlamewingTokenType." + super.toString();
    }
}
