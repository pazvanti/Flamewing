package tech.petrepopescu.flamewing.idea;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

public class FlamewingCompletionContributor extends CompletionContributor {
    public FlamewingCompletionContributor() {
        extend(CompletionType.BASIC,
                PlatformPatterns.psiElement(FlamewingLexer.VARIABLE).withLanguage(FlamewingLanguage.INSTANCE),
                new CompletionProvider<>() {
                    @Override
                    protected void addCompletions(@NotNull CompletionParameters parameters,
                                                  @NotNull ProcessingContext context,
                                                  @NotNull CompletionResultSet resultSet) {
                        resultSet.addElement(LookupElementBuilder.create("if"));
                        resultSet.addElement(LookupElementBuilder.create("for"));
                        resultSet.addElement(LookupElementBuilder.create("args"));
                        resultSet.addElement(LookupElementBuilder.create("import"));
                        resultSet.addElement(LookupElementBuilder.create("csrf.input()"));
                        resultSet.addElement(LookupElementBuilder.create("csrf.meta()"));
                        resultSet.addElement(LookupElementBuilder.create("routes."));
                        resultSet.addElement(LookupElementBuilder.create("with"));
                        resultSet.addElement(LookupElementBuilder.create("break"));
                    }
                }
        );
    }
}
