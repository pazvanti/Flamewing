package tech.petrepopescu.flamewing.parser.elements.builders;

import tech.petrepopescu.flamewing.utils.StringUtils;
import org.springframework.stereotype.Component;
import tech.petrepopescu.flamewing.parser.ElementFactory;
import tech.petrepopescu.flamewing.parser.elements.Element;
import tech.petrepopescu.flamewing.parser.elements.EscapeCharacterElement;

import java.util.List;

@Component
public class EscapeCharacterElementBuilder extends ElementBuilder{
    @Override
    public boolean isValid(String line) {
        return StringUtils.startsWith(line, "@@");
    }

    @Override
    public Element buildFromLine(List<String> lines, int lineNumber, ElementFactory elementFactory, String builderName) {
        return new EscapeCharacterElement(lines, lineNumber, elementFactory, builderName);
    }
}
