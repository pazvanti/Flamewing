package tech.petrepopescu.flamewing.idea;

import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.*;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

public class FlamewingReferenceContributor extends PsiReferenceContributor {
    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
        // Registering a broad reference provider 
        registrar.registerReferenceProvider(PlatformPatterns.psiElement(),
                new PsiReferenceProvider() {
                    @NotNull
                    @Override
                    public PsiReference[] getReferencesByElement(@NotNull PsiElement element,
                                                                 @NotNull ProcessingContext context) {
                        // In a fully developed grammar, this would check if element is a ROUTE_REFERENCE node
                        // and return a PsiReferenceBase pointing to the corresponding Java Method.
                        return PsiReference.EMPTY_ARRAY;
                    }
                });
    }
}
