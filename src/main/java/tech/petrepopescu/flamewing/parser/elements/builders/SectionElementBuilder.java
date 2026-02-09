package tech.petrepopescu.flamewing.parser.elements.builders;

import tech.petrepopescu.flamewing.utils.StringUtils;
import org.springframework.stereotype.Component;
import tech.petrepopescu.flamewing.parser.ElementFactory;
import tech.petrepopescu.flamewing.parser.elements.Element;
import tech.petrepopescu.flamewing.parser.elements.SectionElement;

import java.util.List;

@Component
public class SectionElementBuilder extends ContainerElementBuilders {
    @Override
    public boolean isValid(String line) {
        return StringUtils.startsWith(line, "@section(");
    }

    @Override
    public Element buildFromLine(List<String> lines, int lineNumber, ElementFactory elementFactory, String builderName) {
        return new SectionElement(lines, lineNumber, elementFactory, builderName);
    }
}
