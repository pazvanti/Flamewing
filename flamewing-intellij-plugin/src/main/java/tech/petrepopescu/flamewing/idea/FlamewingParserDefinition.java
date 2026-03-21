package tech.petrepopescu.flamewing.idea;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;

public class FlamewingParserDefinition implements ParserDefinition {
    public static final IFileElementType FILE = new IFileElementType(FlamewingLanguage.INSTANCE);

    @NotNull
    @Override
    public Lexer createLexer(Project project) {
        return new FlamewingLexer();
    }

    @Override
    public PsiParser createParser(Project project) {
        return (root, builder) -> {
            com.intellij.lang.PsiBuilder.Marker rootMarker = builder.mark();
            while (!builder.eof()) {
                com.intellij.lang.PsiBuilder.Marker tokenMarker = builder.mark();
                com.intellij.psi.tree.IElementType type = builder.getTokenType();
                builder.advanceLexer();
                tokenMarker.done(type != null ? type : FlamewingLexer.TEXT);
            }
            rootMarker.done(root);
            return builder.getTreeBuilt();
        };
    }

    @Override
    public IFileElementType getFileNodeType() {
        return FILE;
    }

    @NotNull
    @Override
    public TokenSet getWhitespaceTokens() {
        return TokenSet.EMPTY;
    }

    @NotNull
    @Override
    public TokenSet getCommentTokens() {
        return TokenSet.EMPTY;
    }

    @NotNull
    @Override
    public TokenSet getStringLiteralElements() {
        return TokenSet.EMPTY;
    }

    @NotNull
    @Override
    public PsiElement createElement(ASTNode node) {
        return new com.intellij.extapi.psi.ASTWrapperPsiElement(node);
    }

    @Override
    public PsiFile createFile(FileViewProvider viewProvider) {
        return new com.intellij.extapi.psi.PsiFileBase(viewProvider, FlamewingLanguage.INSTANCE) {
            @NotNull
            @Override
            public com.intellij.openapi.fileTypes.FileType getFileType() {
                return FlamewingFileType.INSTANCE;
            }
        };
    }
}
