package tech.petrepopescu.flamewing.idea;

import com.intellij.lexer.LexerBase;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FlamewingLexer extends LexerBase {
    public static final IElementType KEYWORD = new FlamewingTokenType("KEYWORD");
    public static final IElementType TEXT = new FlamewingTokenType("TEXT");
    public static final IElementType VARIABLE = new FlamewingTokenType("VARIABLE");

    private CharSequence buffer;
    private int startOffset;
    private int endOffset;
    private int currentPosition;
    private IElementType currentToken;
    private int tokenStart;

    @Override
    public void start(@NotNull CharSequence buffer, int startOffset, int endOffset, int initialState) {
        this.buffer = buffer;
        this.startOffset = startOffset;
        this.endOffset = endOffset;
        this.currentPosition = startOffset;
        advance();
    }

    @Override
    public int getState() {
        return 0;
    }

    @Nullable
    @Override
    public IElementType getTokenType() {
        return currentToken;
    }

    @Override
    public int getTokenStart() {
        return tokenStart;
    }

    @Override
    public int getTokenEnd() {
        return currentPosition;
    }

    @Override
    public void advance() {
        if (currentPosition >= endOffset) {
            currentToken = null;
            return;
        }

        tokenStart = currentPosition;
        char c = buffer.charAt(currentPosition);

        if (c == '@') {
            currentPosition++;
            while (currentPosition < endOffset) {
                char nextContent = buffer.charAt(currentPosition);
                // Keep parsing variable/keyword characters until space, brace, etc.
                if (Character.isWhitespace(nextContent) || nextContent == '(' || nextContent == '{' || nextContent == '<' || nextContent == '?') {
                    break;
                }
                currentPosition++;
            }
            
            String tokenText = buffer.subSequence(tokenStart, currentPosition).toString();
            if (tokenText.equals("@if") || tokenText.equals("@for") || tokenText.equals("@args") || 
                tokenText.equals("@import") || tokenText.equals("@with") || tokenText.equals("@break")) {
                currentToken = KEYWORD;
            } else {
                currentToken = VARIABLE;
            }
        } else {
            // Parse as text until next @
            while (currentPosition < endOffset && buffer.charAt(currentPosition) != '@') {
                currentPosition++;
            }
            currentToken = TEXT;
        }
    }

    @NotNull
    @Override
    public CharSequence getBufferSequence() {
        return buffer;
    }

    @Override
    public int getBufferEnd() {
        return endOffset;
    }
}
