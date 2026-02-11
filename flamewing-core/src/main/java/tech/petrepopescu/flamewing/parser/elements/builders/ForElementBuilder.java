package tech.petrepopescu.flamewing.parser.elements.builders;

import org.springframework.stereotype.Component;
import tech.petrepopescu.flamewing.parser.ElementFactory;
import tech.petrepopescu.flamewing.parser.elements.Element;
import tech.petrepopescu.flamewing.parser.elements.NestedElement;

import java.util.List;

@Component
public class ForElementBuilder extends ContainerElementBuilders {
    @Override
    public boolean isValid(String line) {
        return line.trim().startsWith("@for");
    }

    @Override
    public Element buildFromLine(List<String> lines, int lineNumber, ElementFactory elementFactory, String builderName) {
        return new NestedElement(lines, lineNumber, elementFactory, "for", builderName);
    }

    @Override
    public int order() {
        return 200;
    }
}
