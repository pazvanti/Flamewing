package tech.petrepopescu.flamewing.parser.elements;

import tech.petrepopescu.flamewing.utils.StringUtils;
import tech.petrepopescu.flamewing.parser.ElementFactory;

import java.util.List;

public class RawElement extends Element {
    public RawElement(List<String> lines, int lineIndex, ElementFactory elementFactory, String builderName) {
        super(lines, lineIndex, elementFactory, builderName);
    }

    @Override
    public int parse(String fileName) {
        String line = StringUtils.trim(this.lines.get(this.lineNumber));
        int indexOfVariableEnd = indexOfElementEnd(line, 0);
        String variableName = StringUtils.substring(line, 5, indexOfVariableEnd - 1);
        appendWithContentBuilder(variableName);
        discoverNextElement(StringUtils.substring(line, indexOfVariableEnd), fileName);
        return lineNumber;
    }

    @Override
    public StringBuilder write() {
        if (this.nextElement != null) {
            this.contentBuilder.append(this.nextElement.write());
        }
        return this.contentBuilder;
    }
}
