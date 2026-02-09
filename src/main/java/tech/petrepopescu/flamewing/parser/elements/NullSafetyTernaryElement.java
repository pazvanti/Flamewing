package tech.petrepopescu.flamewing.parser.elements;

import tech.petrepopescu.flamewing.utils.StringUtils;
import tech.petrepopescu.flamewing.parser.ElementFactory;

import java.util.List;
import java.util.UUID;

public class NullSafetyTernaryElement extends Element {
    public NullSafetyTernaryElement(List<String> lines, int lineIndex, ElementFactory elementFactory, String builderName) {
        super(lines, lineIndex, elementFactory, builderName);
    }

    @Override
    public int parse(String fileName) {
        String line = lines.get(lineNumber).trim();
        int variableEnd = StringUtils.indexOf(line, '?');
        String variableExpression = StringUtils.trim(StringUtils.substring(line, 1, variableEnd));
        int numSpaces = variableEnd - variableExpression.length();
        VariableElement variableElement = new VariableElement(List.of("@" + variableExpression), 0, elementFactory, builderName);
        variableElement.tabs(this.numTabs + 1);
        variableElement.parse(fileName);
        String remainingExpression = StringUtils.trim(StringUtils.substring(line, variableEnd + 2));
        int elementEnd = indexOfElementEnd(remainingExpression, 0, true) + 1;
        String alternativeExpression = StringUtils.trim(StringUtils.substring(remainingExpression, 0, elementEnd));
        numSpaces += StringUtils.indexOf(line, alternativeExpression, variableEnd + 2) - (variableEnd + 2) - 1;

        String varName = "tmpVar_" + StringUtils.remove(UUID.randomUUID().toString(), '-');
        appendAsCode("var " + varName + " = " + variableElement.getEvalExpression() + ";\n");
        appendAsCode("if (" + varName + " != null) {\n");
        appendWithContentBuilder(varName, this.numTabs + 1);
        appendAsCode("} else {\n");
        appendWithContentBuilder(alternativeExpression, this.numTabs + 1);
        appendAsCode("}\n");
        discoverNextElement(StringUtils.substring(line, variableEnd + 2 + elementEnd + numSpaces), fileName);
        return this.lineNumber;
    }

    @Override
    public StringBuilder write() {
        if (this.nextElement != null) {
            this.contentBuilder.append(this.nextElement.write());
        }
        return this.contentBuilder;
    }
}
