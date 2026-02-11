package tech.petrepopescu.flamewing.parser.elements;

import tech.petrepopescu.flamewing.utils.StringUtils;
import tech.petrepopescu.flamewing.exception.ParsingException;
import tech.petrepopescu.flamewing.parser.ElementFactory;

import java.util.List;

public class ForWithIteratorElement extends NestedElement {
    private String iteratorName;
    private String itemType = "Object";
    private String itemName;
    private String collectionName;

    public ForWithIteratorElement(List<String> lines, int lineIndex, ElementFactory elementFactory, String builderName) {
        super(lines, lineIndex, elementFactory, "for", builderName);
    }

    @Override
    public int parse(String fileName) {
        String line = this.lines.get(this.lineNumber);
        extractAttributes(line);
        parseContentInside(line, fileName);

        return this.lineNumber;
    }

    private void extractAttributes(String line) {
        int indexOfStart = StringUtils.indexOf(line, "((") + 2;
        int indexOfDeclarationEnd = StringUtils.indexOf(line, ")");

        String declaration = StringUtils.substring(line, indexOfStart, indexOfDeclarationEnd);
        String[] declarationParts = declaration.split(",");

        if (declarationParts.length != 2) {
            throw new ParsingException("Invalid for-with-iterator statement: " + line);
        }

        String[] iteratorNameParts = StringUtils.split(StringUtils.trim(declarationParts[0]), ' ');
        if (iteratorNameParts.length > 2) {
            throw new ParsingException("Invalid for-with-iterator statement: " + line);
        }
        if (iteratorNameParts.length == 1) {
            this.iteratorName = StringUtils.trim(iteratorNameParts[0]);
        } else {
            this.iteratorName = StringUtils.trim(iteratorNameParts[1]);
        }

        String[] itemNameParts = StringUtils.split(StringUtils.trim(declarationParts[1]), ' ');
        if (itemNameParts.length > 2) {
            throw new ParsingException("Invalid for-with-iterator statement: " + line);
        }
        if (itemNameParts.length == 1) {
            this.itemName = StringUtils.trim(itemNameParts[0]);
        } else {
            this.itemType = StringUtils.trim(itemNameParts[0]);
            this.itemName = StringUtils.trim(itemNameParts[1]);
        }

        String declarationString = StringUtils.substring(line, indexOfDeclarationEnd + 1);
        int indexOfCollectionNameStart = StringUtils.indexOf(declarationString, ":") + 1;
        if (indexOfCollectionNameStart < 1) {
            indexOfCollectionNameStart = StringUtils.indexOf(declarationString, " in ") + 4;
        }
        int indexOfCollectionNameEnd = StringUtils.lastIndexOf(declarationString, ")");
        this.collectionName = StringUtils.trim(StringUtils.substring(declarationString, indexOfCollectionNameStart, indexOfCollectionNameEnd));
    }

    @Override
    public StringBuilder write() {
        appendAsCode("{\n");
        numTabs++;
        appendAsCode("ForIterator<" + itemType + "> " + iteratorName + " = new ForIterator(" + collectionName + ".iterator());\n");
        appendAsCode(itemType + " " + itemName + " = null;\n");
        appendAsCode("while (" + iteratorName + ".hasNext()) {\n");
        appendAsCode("\t" + itemName + " = " + iteratorName + ".nextNoIncrease();\n");
        for (Element element:this.nestedElements) {
            this.contentBuilder.append(element.write());
        }
        appendAsCode(iteratorName + ".increase();\n");
        this.contentBuilder.append(StringUtils.repeat('\t', this.numTabs)).append("}\n");
        if (this.nextElement != null) {
            this.contentBuilder.append(this.nextElement.write());
        }
        numTabs--;
        appendAsCode("}\n");
        return this.contentBuilder;
    }

}
