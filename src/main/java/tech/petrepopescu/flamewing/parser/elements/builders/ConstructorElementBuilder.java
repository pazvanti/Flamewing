package tech.petrepopescu.flamewing.parser.elements.builders;

import tech.petrepopescu.flamewing.utils.StringUtils;
import org.springframework.stereotype.Component;
import tech.petrepopescu.flamewing.parser.ElementFactory;
import tech.petrepopescu.flamewing.parser.elements.ConstructorElement;
import tech.petrepopescu.flamewing.parser.elements.Element;

import java.util.List;

@Component
public class ConstructorElementBuilder extends ElementBuilder {
    @Override
    public boolean isValid(String line) {
        return StringUtils.startsWithAny(line, "@args(", "@args (");
    }

    @Override
    public Element buildFromLine(List<String> lines, int lineNumber, ElementFactory elementFactory, String builderName) {
        return new ConstructorElement(lines, lineNumber, elementFactory, builderName);
    }
}
