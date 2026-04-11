package tech.petrepopescu.flamewing.parser.elements.builders;

import org.springframework.stereotype.Component;
import tech.petrepopescu.flamewing.parser.ElementFactory;
import tech.petrepopescu.flamewing.parser.elements.Element;
import tech.petrepopescu.flamewing.parser.elements.NewElement;

import java.util.List;

@Component
public class NewElementBuilder extends ElementBuilder {
    @Override
    public boolean isValid(String line) {
        return line.trim().startsWith("@new");
    }

    @Override
    public Element buildFromLine(List<String> lines, int lineNumber, ElementFactory elementFactory, String builderName) {
        return new NewElement(lines, lineNumber, elementFactory, builderName);
    }

    @Override
    public int order() {
        return 10;
    }
}
