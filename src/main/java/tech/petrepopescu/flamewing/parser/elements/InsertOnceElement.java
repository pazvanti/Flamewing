package tech.petrepopescu.flamewing.parser.elements;

import tech.petrepopescu.flamewing.parser.ElementFactory;

import java.util.List;

public class InsertOnceElement extends InsertAtElement {
    public InsertOnceElement(List<String> lines, int lineIndex, ElementFactory elementFactory, String builderName) {
        super(lines, lineIndex, elementFactory, builderName);
    }

    @Override
    public StringBuilder write() {
        appendAsCode("if (this.contentGroup.isSectionInserted(\"" + this.getSectionName() + "\") == false) {\n");
        for (Element element:this.nestedElements) {
            this.contentBuilder.append(element.write());
        }
        appendAsCode("\tthis.contentGroup.markSectionAsInserted(\"" + this.getSectionName() + "\");\n");
        appendAsCode("} \n");
        return this.contentBuilder;
    }
}
