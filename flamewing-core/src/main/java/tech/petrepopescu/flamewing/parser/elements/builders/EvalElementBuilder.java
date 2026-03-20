package tech.petrepopescu.flamewing.parser.elements.builders;

import tech.petrepopescu.flamewing.utils.StringUtils;
import tech.petrepopescu.flamewing.parser.ElementFactory;
import tech.petrepopescu.flamewing.parser.elements.Element;
import tech.petrepopescu.flamewing.parser.elements.EvalElement;

import java.util.List;

public class EvalElementBuilder extends ElementBuilder {
    @Override
    public boolean isValid(String line) {
        return StringUtils.startsWith(line, "@(") || StringUtils.startsWith(line, "@ (");
    }

    @Override
    public Element buildFromLine(List<String> lines, int lineNumber, ElementFactory elementFactory, String builderName) {
        return new EvalElement(lines, lineNumber, elementFactory, builderName);
    }
}
