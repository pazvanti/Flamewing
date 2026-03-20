package tech.petrepopescu.flamewing.parser.elements;

import tech.petrepopescu.flamewing.utils.StringUtils;
import tech.petrepopescu.flamewing.parser.ElementFactory;

import java.util.List;

public class ElseElement extends NestedElement {
    public ElseElement(List<String> fileLines, int lineIndex, ElementFactory elementFactory, String builderName) {
        super(fileLines, lineIndex, elementFactory, "else", builderName);
    }

    @Override
    public int parse(String fileName) {
        String line = StringUtils.trim(this.lines.get(this.lineNumber));
        if (StringUtils.startsWith(line, "}")) {
            line = StringUtils.substring(line, 1);
        }
        parseContentInside(line, fileName);

        return this.lineNumber;
    }

    @Override
    public StringBuilder write() {
        this.contentBuilder.append(" else {\n");
        for (Element element:this.nestedElements) {
            this.contentBuilder.append(element.write());
        }
        this.contentBuilder.append(StringUtils.repeat('\t', this.numTabs)).append("}\n");
        if (this.nextElement != null) {
            this.contentBuilder.append(this.nextElement.write());
        }
        return this.contentBuilder;
    }
}
