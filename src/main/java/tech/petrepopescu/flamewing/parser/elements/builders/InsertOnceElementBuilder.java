package tech.petrepopescu.flamewing.parser.elements.builders;

import tech.petrepopescu.flamewing.utils.StringUtils;
import org.springframework.stereotype.Component;
import tech.petrepopescu.flamewing.parser.ElementFactory;
import tech.petrepopescu.flamewing.parser.elements.Element;
import tech.petrepopescu.flamewing.parser.elements.InsertOnceElement;

import java.util.List;

@Component
public class InsertOnceElementBuilder extends ContainerElementBuilders {
    @Override
    public boolean isValid(String line) {
        return StringUtils.startsWith(line, "@insertOnce(");
    }

    @Override
    public Element buildFromLine(List<String> lines, int lineNumber, ElementFactory elementFactory, String builderName) {
        String sectionName = StringUtils.substringBetween(lines.get(lineNumber), "insertOnce(\"", "\")");
        return new InsertOnceElement(lines, lineNumber, elementFactory, sectionName + "ContentBuilder");
    }
}
