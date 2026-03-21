package tech.petrepopescu.flamewing.idea;

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import com.intellij.lexer.Lexer;
import org.jetbrains.annotations.NotNull;

public class FlamewingHighlighter extends SyntaxHighlighterBase {
    public static final TextAttributesKey KEYWORD =
            TextAttributesKey.createTextAttributesKey("FLAMEWING_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD);
    public static final TextAttributesKey VARIABLE =
            TextAttributesKey.createTextAttributesKey("FLAMEWING_VARIABLE", DefaultLanguageHighlighterColors.INSTANCE_FIELD);
    public static final TextAttributesKey TEXT =
            TextAttributesKey.createTextAttributesKey("FLAMEWING_TEXT", DefaultLanguageHighlighterColors.TEMPLATE_LANGUAGE_COLOR);

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        return new FlamewingLexer();
    }

    @NotNull
    @Override
    public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
        if (tokenType.equals(FlamewingLexer.KEYWORD)) {
            return new TextAttributesKey[]{KEYWORD};
        } else if (tokenType.equals(FlamewingLexer.VARIABLE)) {
            return new TextAttributesKey[]{VARIABLE};
        }
        return new TextAttributesKey[]{TEXT};
    }
}
