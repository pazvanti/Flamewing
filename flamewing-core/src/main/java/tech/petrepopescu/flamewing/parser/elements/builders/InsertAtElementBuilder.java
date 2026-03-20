package tech.petrepopescu.flamewing.parser.elements.builders;

import tech.petrepopescu.flamewing.utils.StringUtils;
import tech.petrepopescu.flamewing.parser.ElementFactory;
import tech.petrepopescu.flamewing.parser.elements.Element;
import tech.petrepopescu.flamewing.parser.elements.InsertAtElement;

import java.util.List;

public class InsertAtElementBuilder extends ContainerElementBuilders {
    @Override
    public boolean isValid(String line) {
        return StringUtils.startsWith(line, "@insertAt(");
    }

    @Override
    public Element buildFromLine(List<String> lines, int lineNumber, ElementFactory elementFactory, String builderName) {
        String sectionName = StringUtils.substringBetween(lines.get(lineNumber), "insertAt(\"", "\")");
        return new InsertAtElement(lines, lineNumber, elementFactory, sectionName + "ContentBuilder");
    }
}
