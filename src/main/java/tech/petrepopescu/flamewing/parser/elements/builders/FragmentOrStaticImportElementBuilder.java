package tech.petrepopescu.flamewing.parser.elements.builders;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import tech.petrepopescu.flamewing.exception.ParsingException;
import tech.petrepopescu.flamewing.parser.ElementFactory;
import tech.petrepopescu.flamewing.parser.elements.Element;
import tech.petrepopescu.flamewing.parser.elements.FragmentOrStaticImportCallElement;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;

@Component
public class FragmentOrStaticImportElementBuilder extends ElementBuilder {
    private final Set<String> fragmentDefinitions = new HashSet<>();
    private final Set<String> fragmentsInCode = new HashSet<>();
    @Override
    public boolean isValid(String line) {
        return true;
    }

    public void attachNewFragment(String name) {
        fragmentDefinitions.add(name);
    }

    public void discoveredFragmentCall(String fragmentInCode) {
        fragmentsInCode.add("views.html." + fragmentInCode);
    }

    public void validateFragments() {
        Set<String> unknownFragments = new HashSet<>();
        for (String inCode:fragmentsInCode) {
            if (!fragmentDefinitions.contains(inCode)) {
                unknownFragments.add(inCode);
            }
        }

        if (!CollectionUtils.isEmpty(unknownFragments)) {
            StringJoiner stringJoiner = new StringJoiner(",");
            unknownFragments.forEach(stringJoiner::add);
            throw new ParsingException("Discovered unknown fragments calls: " + stringJoiner);
        }
    }

    @Override
    public Element buildFromLine(List<String> lines, int lineNumber, ElementFactory elementFactory, String builderName) {
        return new FragmentOrStaticImportCallElement(lines, lineNumber, elementFactory, builderName);
    }

    @Override
    public int order() {
        return 9000;
    }
}
