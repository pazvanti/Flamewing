package tech.petrepopescu.flamewing.parser.elements;

import tech.petrepopescu.flamewing.utils.StringUtils;
import tech.petrepopescu.flamewing.parser.ElementFactory;

import java.util.List;

public class InsertAtElement extends AbstractContainerElement {
    private final String sectionName;
    public InsertAtElement(List<String> lines, int lineIndex, ElementFactory elementFactory, String builderName) {
        super(lines, lineIndex, elementFactory, builderName);
        this.sectionName = StringUtils.substring(builderName, 0, builderName.length() - "ContentBuilder".length());
    }

    @Override
    public int parse(String fileName) {
        String line = this.lines.get(this.lineNumber);
        parseContentInside(line, fileName);
        return this.lineNumber;
    }

    @Override
    public StringBuilder write() {
        for (Element element:this.nestedElements) {
            this.contentBuilder.append(element.write());
        }
        return this.contentBuilder;
    }

    @Override
    public String getSectionName() {
        return this.sectionName;
    }
}
