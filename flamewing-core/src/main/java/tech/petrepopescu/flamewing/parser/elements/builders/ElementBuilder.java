package tech.petrepopescu.flamewing.parser.elements.builders;

import tech.petrepopescu.flamewing.parser.ElementFactory;
import tech.petrepopescu.flamewing.parser.elements.Element;

import java.util.List;

public abstract class ElementBuilder {
    public int order() {
        return 0;
    }
    public abstract boolean isValid(String line);
    public abstract Element buildFromLine(List<String> lines, int lineNumber, ElementFactory elementFactory, String builderName);
}
