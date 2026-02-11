package tech.petrepopescu.flamewing.parser.elements;

import tech.petrepopescu.flamewing.utils.StringUtils;
import tech.petrepopescu.flamewing.parser.ElementFactory;

import java.util.List;

public class RouteElement extends Element {
    private String routeVal;

    public RouteElement(List<String> lines, int lineIndex, ElementFactory elementFactory, String builderName) {
        super(lines, lineIndex, elementFactory, builderName);
    }

    @Override
    public int parse(String fileName) {
        String line = StringUtils.trim(this.lines.get(this.lineNumber));
        int indexOfVariableEnd = indexOfElementEnd(line, StringUtils.indexOf(line, "("), true, List.of(')'));
        routeVal = StringUtils.substring(line, 1, indexOfVariableEnd);
        discoverNextElement(StringUtils.substring(line, indexOfVariableEnd), fileName);
        return lineNumber;
    }

    @Override
    public StringBuilder write() {
        this.contentBuilder.append(StringUtils.repeat('\t', this.numTabs));
        this.contentBuilder.append(builderName).append(".append(").append(routeVal).append(".path());\n");
        if (this.nextElement != null) {
            this.contentBuilder.append(this.nextElement.write());
        }
        return this.contentBuilder;
    }
}
