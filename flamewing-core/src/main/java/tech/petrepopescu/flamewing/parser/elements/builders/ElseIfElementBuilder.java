package tech.petrepopescu.flamewing.parser.elements.builders;

import tech.petrepopescu.flamewing.utils.StringUtils;
import org.springframework.stereotype.Component;
import tech.petrepopescu.flamewing.parser.ElementFactory;
import tech.petrepopescu.flamewing.parser.elements.Element;
import tech.petrepopescu.flamewing.parser.elements.ElseIfElement;

import java.util.List;

@Component
public class ElseIfElementBuilder extends ContainerElementBuilders {
    @Override
    public boolean isValid(String line) {
        return StringUtils.startsWithAny(line, " else if (", "else if(", " else if(", "else if (") ||
                StringUtils.startsWithAny(line, "} else if (", "}else if(", "} else if(", "}else if (");
    }

    @Override
    public Element buildFromLine(List<String> lines, int lineNumber, ElementFactory elementFactory, String builderName) {
        return new ElseIfElement(lines, lineNumber, elementFactory, builderName);
    }
}
