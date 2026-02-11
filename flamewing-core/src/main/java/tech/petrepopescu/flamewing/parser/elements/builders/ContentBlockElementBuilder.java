package tech.petrepopescu.flamewing.parser.elements.builders;

import tech.petrepopescu.flamewing.utils.StringUtils;
import org.springframework.stereotype.Component;
import tech.petrepopescu.flamewing.parser.ElementFactory;
import tech.petrepopescu.flamewing.parser.elements.ContentBlockElement;
import tech.petrepopescu.flamewing.parser.elements.Element;

import java.util.List;

@Component
public class ContentBlockElementBuilder extends ElementBuilder {
    @Override
    public boolean isValid(String line) {
        if (!StringUtils.startsWith(line, "@")) {
            return false;
        }

        int idxOfArrow = StringUtils.indexOf(line, "=>", 2);
        if (idxOfArrow < 0) {
            return false;
        }

        return StringUtils.indexOf(line, "{") > idxOfArrow;
    }

    @Override
    public Element buildFromLine(List<String> lines, int lineNumber, ElementFactory elementFactory, String builderName) {
        return new ContentBlockElement(lines, lineNumber, elementFactory, builderName);
    }

    @Override
    public int order() {
        return 900;
    }
}
