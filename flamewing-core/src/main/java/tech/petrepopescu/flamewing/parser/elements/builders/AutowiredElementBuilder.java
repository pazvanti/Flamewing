package tech.petrepopescu.flamewing.parser.elements.builders;

import tech.petrepopescu.flamewing.utils.StringUtils;
import tech.petrepopescu.flamewing.parser.ElementFactory;
import tech.petrepopescu.flamewing.parser.elements.AutowiredElement;
import tech.petrepopescu.flamewing.parser.elements.Element;

import java.util.List;

public class AutowiredElementBuilder extends ElementBuilder {
    @Override
    public boolean isValid(String line) {
        return StringUtils.startsWith(line, "@autowired(");
    }

    @Override
    public Element buildFromLine(List<String> lines, int lineNumber, ElementFactory elementFactory, String builderName) {
        return new AutowiredElement(lines, lineNumber, elementFactory, builderName);
    }
}
