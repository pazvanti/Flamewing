package tech.petrepopescu.flamewing.parser.elements.builders;

import org.springframework.stereotype.Component;
import tech.petrepopescu.flamewing.parser.ElementFactory;
import tech.petrepopescu.flamewing.parser.elements.WithElement;
import tech.petrepopescu.flamewing.parser.elements.Element;

import java.util.List;

@Component
public class WithElementBuilder extends ContainerElementBuilders {
    @Override
    public boolean isValid(String line) {
        return line.trim().startsWith("@with");
    }

    @Override
    public Element buildFromLine(List<String> lines, int lineNumber, ElementFactory elementFactory, String builderName) {
        return new WithElement(lines, lineNumber, elementFactory, builderName);
    }
}
