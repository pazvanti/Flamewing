package tech.petrepopescu.flamewing.parser.elements.builders;

import tech.petrepopescu.flamewing.utils.StringUtils;
import org.springframework.stereotype.Component;
import tech.petrepopescu.flamewing.parser.ElementFactory;
import tech.petrepopescu.flamewing.parser.elements.ForWithIteratorElement;
import tech.petrepopescu.flamewing.parser.elements.Element;

import java.util.List;

@Component
public class ForWithIteratorElementBuilder extends ContainerElementBuilders {
    @Override
    public boolean isValid(String line) {
        return StringUtils.startsWith(line, "@for((") || StringUtils.startsWith(line, "@for ((");
    }

    @Override
    public Element buildFromLine(List<String> lines, int lineNumber, ElementFactory elementFactory, String builderName) {
        return new ForWithIteratorElement(lines, lineNumber, elementFactory, builderName);
    }

    @Override
    public int order() {
        return 100;
    }
}
