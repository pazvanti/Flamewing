package tech.petrepopescu.flamewing.parser.elements.builders;

import tech.petrepopescu.flamewing.utils.StringUtils;
import org.springframework.stereotype.Component;
import tech.petrepopescu.flamewing.parser.ElementFactory;
import tech.petrepopescu.flamewing.parser.elements.Element;
import tech.petrepopescu.flamewing.parser.elements.VariableElement;

import java.util.List;

@Component
public class VariableElementBuilder extends ElementBuilder {
    @Override
    public boolean isValid(String line) {
        if (!StringUtils.startsWith(line, "@")) {
            return false;
        }
        int idxOfWhitespace = StringUtils.indexOf(line, ' ', 1);
        String stringToCheck = line;
        if (idxOfWhitespace > 0) {
            stringToCheck = StringUtils.substring(line, 0, idxOfWhitespace);
        }

        int indexOfDot = StringUtils.indexOf(stringToCheck, ".", 1);
        if (indexOfDot > 0) {
            int indexOfFunctionCall = StringUtils.indexOf(stringToCheck, "(", 1);
            if (indexOfFunctionCall > 0) {
                if (indexOfDot < indexOfFunctionCall) {
                    String dotToFunctionCall = StringUtils.substring(line, indexOfDot, indexOfFunctionCall);
                    String functionName = StringUtils.substring(dotToFunctionCall, StringUtils.lastIndexOf(dotToFunctionCall, ".") + 1);
                    return !"template".equals(functionName);
                }
                return false;
            }
            return true;
        }
        return !StringUtils.contains(stringToCheck, "(");
    }

    @Override
    public Element buildFromLine(List<String> lines, int lineNumber, ElementFactory elementFactory, String builderName) {
        return new VariableElement(lines, lineNumber, elementFactory, builderName);
    }

    @Override
    public int order() {
        return 1000;
    }
}
