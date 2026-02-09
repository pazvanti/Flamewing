package tech.petrepopescu.flamewing.parser.elements;

import tech.petrepopescu.flamewing.utils.StringUtils;
import tech.petrepopescu.flamewing.parser.ElementFactory;

import java.util.List;

public class IfElement extends NestedElement {
    public IfElement(List<String> fileLines, int lineIndex, ElementFactory elementFactory, String builderName) {
        super(fileLines, lineIndex, elementFactory, "if", builderName);
    }

    public IfElement(List<String> fileLines, int lineIndex, ElementFactory elementFactory, String type, String builderName) {
        super(fileLines, lineIndex, elementFactory, type, builderName);
    }

    @Override
    public int parse(String fileName) {
        elementFactory.enteringIfStatement();
        int newLineNumber = super.parse(fileName);
        elementFactory.exitingIfStatement();
        return newLineNumber;
    }

    @Override
    public StringBuilder write() {
        this.contentBuilder.append(StringUtils.repeat('\t', this.numTabs)).append(type).append(" (").append(statement).append(") {\n");
        for (Element element:this.nestedElements) {
            this.contentBuilder.append(element.write());
        }
        this.contentBuilder.append(StringUtils.repeat('\t', this.numTabs)).append("}");
        if (!(nextElement instanceof ElseElement || nextElement instanceof ElseIfElement)) {
            this.contentBuilder.append("\n");
        }
        if (this.nextElement != null) {
            this.contentBuilder.append(this.nextElement.write());
        }
        return this.contentBuilder;
    }
}
