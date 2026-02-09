package tech.petrepopescu.flamewing.parser.elements.builders;

import tech.petrepopescu.flamewing.utils.StringUtils;
import org.springframework.stereotype.Component;
import tech.petrepopescu.flamewing.parser.ElementFactory;
import tech.petrepopescu.flamewing.parser.elements.Element;
import tech.petrepopescu.flamewing.parser.elements.ElseElement;

import java.util.List;

@Component
public class ElseElementBuilder extends ContainerElementBuilders {
    @Override
    public boolean isValid(String line) {
        return StringUtils.startsWithAny(line," else {", "else{", " else{", "else {") ||
                StringUtils.startsWithAny(line,"} else {", "}else{", "} else{", "}else {") ;
    }

    @Override
    public Element buildFromLine(List<String> lines, int lineNumber, ElementFactory elementFactory, String builderName) {
        return new ElseElement(lines, lineNumber, elementFactory, builderName);
    }
}
