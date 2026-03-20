package tech.petrepopescu.flamewing.parser.elements;

import tech.petrepopescu.flamewing.utils.StringUtils;
import tech.petrepopescu.flamewing.parser.ElementFactory;

import java.util.List;

public class EscapeCharacterElement extends Element {
    public EscapeCharacterElement(List<String> lines, int lineIndex, ElementFactory elementFactory, String builderName) {
        super(lines, lineIndex, elementFactory, builderName);
    }

    @Override
    public int parse(String fileName) {
        String line = this.lines.get(lineNumber);
        int elementEnd = this.indexOfElementEnd(line, 1);
        appendAsStringWithContentBuilder(StringUtils.substring(line, 1, elementEnd));
        if (elementEnd < line.length()) {
            discoverNextElement(StringUtils.substring(line, elementEnd), fileName);
        }
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
