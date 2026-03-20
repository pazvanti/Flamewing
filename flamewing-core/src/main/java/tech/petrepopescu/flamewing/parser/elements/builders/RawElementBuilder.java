package tech.petrepopescu.flamewing.parser.elements.builders;

import tech.petrepopescu.flamewing.utils.StringUtils;
import org.springframework.stereotype.Component;
import tech.petrepopescu.flamewing.parser.ElementFactory;
import tech.petrepopescu.flamewing.parser.elements.Element;
import tech.petrepopescu.flamewing.parser.elements.RawElement;

import java.util.List;

@Component
public class RawElementBuilder extends ElementBuilder {
    @Override
    public boolean isValid(String line) {
        return StringUtils.startsWith(line, "@raw(");
    }

    @Override
    public Element buildFromLine(List<String> lines, int lineNumber, ElementFactory elementFactory, String builderName) {
        return new RawElement(lines, lineNumber, elementFactory, builderName);
    }
}
