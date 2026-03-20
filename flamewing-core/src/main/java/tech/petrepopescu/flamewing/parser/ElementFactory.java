package tech.petrepopescu.flamewing.parser;


import tech.petrepopescu.flamewing.utils.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import tech.petrepopescu.flamewing.parser.elements.Element;
import tech.petrepopescu.flamewing.parser.elements.HtmlElement;
import tech.petrepopescu.flamewing.parser.elements.builders.*;

import java.util.*;

@Component
public class ElementFactory {
    public static final String DEFAULT_BUILDER_NAME = "contentBuilder";

    private final List<ElementBuilder> elementBuilders;
    private final List<ElementBuilder> elseElementBuilders;
    private final List<String> staticImports;
    private final FragmentOrStaticImportElementBuilder fragmentOrStaticImportElementBuilder;
    private int nestedIfs = 0;

    private String builderName = DEFAULT_BUILDER_NAME;
    public ElementFactory(Set<ElementBuilder> elementBuilders) {
        if (CollectionUtils.isEmpty(elementBuilders)) {
            fragmentOrStaticImportElementBuilder = new FragmentOrStaticImportElementBuilder();
            elementBuilders = Set.of(new EscapeCharacterElementBuilder(), new RouteElementBuilder(), new ConstructorElementBuilder(),
                    new IfElementBuilder(), new AutowiredElementBuilder(), new NullSafeRawElementBuilder(),
                    new ImportElementBuilder(), new VariableElementBuilder(), new AssetElementBuilder(),
                    new HtmlElementBuilder(), new CommentElementBuilder(), new ForElementBuilder(), new CsrfElementBuilder(),
                    new BreakElementBuilder(), new WithElementBuilder(), new EvalElementBuilder(), new RawElementBuilder(),
                    new NullSafeVariableElementBuilder(), new NullSafeTernaryElementBuilder(), new ContentBlockElementBuilder(),
                    new SectionElementBuilder(), new InsertAtElementBuilder(), new InsertOnceElementBuilder(),
                    new ForWithIteratorElementBuilder(), fragmentOrStaticImportElementBuilder);
            elseElementBuilders = List.of(new ElseElementBuilder(), new ElseIfElementBuilder());
        } else {
            elseElementBuilders = elementBuilders.stream().filter(
                    (builder) -> builder instanceof ElseElementBuilder || builder instanceof ElseIfElementBuilder
            ).toList();

            fragmentOrStaticImportElementBuilder = (FragmentOrStaticImportElementBuilder) elementBuilders.stream().filter(FragmentOrStaticImportElementBuilder.class::isInstance).findFirst().orElse(null);
        }
        this.elementBuilders = elementBuilders.stream().filter(builder ->
                    !(builder instanceof ElseElementBuilder || builder instanceof ElseIfElementBuilder)
                )
                .sorted(Comparator.comparingInt(ElementBuilder::order)).toList();
        this.staticImports = new ArrayList<>();
    }
    public Element getElement(List<String> lines, int lineNumber) {
        String line = StringUtils.trim(lines.get(lineNumber));
        if (nestedIfs > 0) {
            for (ElementBuilder factory:elseElementBuilders) {
                if (factory.isValid(line)) {
                    return factory.buildFromLine(lines, lineNumber, this, builderName);
                }
            }
        }
        for (ElementBuilder factory:elementBuilders) {
            if (factory.isValid(line)) {
                return factory.buildFromLine(lines, lineNumber, this, builderName);
            }
        }

        return new HtmlElement(lines, lineNumber, this, builderName);
    }

    public Element getElement(String line) {
        return getElement(Collections.singletonList(line), 0);
    }

    public void defineNewFragment(String name) {
        fragmentOrStaticImportElementBuilder.attachNewFragment(name);
    }

    public void potentialFragmentCall(String fragmentName) {
        fragmentOrStaticImportElementBuilder.discoveredFragmentCall(fragmentName);
    }

    public void unknownFragmentsExists() {
        fragmentOrStaticImportElementBuilder.validateFragments();
    }

    public void addStaticImport(String staticImport) {
        this.staticImports.add(staticImport);
    }

    public boolean isStaticImport(String staticImport) {
        return this.staticImports.contains(staticImport);
    }

    public void setBuilderName(String builderName) {
        this.builderName = builderName;
    }

    public void resetBuilderName() {
        this.builderName = DEFAULT_BUILDER_NAME;
    }

    public void enteringIfStatement() {
        nestedIfs++;
    }

    public void exitingIfStatement() {
        nestedIfs--;
    }
}
