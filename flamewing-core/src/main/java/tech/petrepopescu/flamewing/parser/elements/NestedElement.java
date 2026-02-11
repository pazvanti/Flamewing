package tech.petrepopescu.flamewing.parser.elements;

import tech.petrepopescu.flamewing.utils.StringUtils;
import tech.petrepopescu.flamewing.parser.ElementFactory;

import java.util.List;
import java.util.UUID;

public class NestedElement extends AbstractContainerElement {
    protected String statement;
    protected final String type;

    public NestedElement(List<String> fileLines, int lineIndex, ElementFactory elementFactory, String type, String builderName) {
        super(fileLines, lineIndex, elementFactory, builderName);
        this.type = type;
    }

    @Override
    public int parse(String fileName) {
        String line = this.lines.get(this.lineNumber);
        this.statement = extractStatement(line);
        parseContentInside(line, fileName);

        return this.lineNumber;
    }

    @Override
    public StringBuilder write() {
        this.contentBuilder.append(StringUtils.repeat('\t', this.numTabs)).append(type).append(" (").append(statement).append(") {\n");
        for (Element element:nestedElements) {
            this.contentBuilder.append(element.write());
        }
        this.contentBuilder.append(StringUtils.repeat('\t', this.numTabs)).append("}\n");
        if (this.nextElement != null) {
            this.contentBuilder.append(this.nextElement.write());
        }
        return this.contentBuilder;
    }

    protected String extractStatement(String line) {
        return StringUtils.substring(line, StringUtils.indexOf(line, '(') + 1, StringUtils.lastIndexOf(line, ')'));
    }

    @Override
    public StringBuilder getFragmentOrStaticImportCallElementWriter(String sectionName) {
        StringBuilder fragmentCallElementWriter = new StringBuilder();
        if (sectionName == null || nestedElements.isEmpty() || this.statement == null) {
            return fragmentCallElementWriter;
        }
        fragmentCallElementWriter.append(StringUtils.repeat('\t', this.numTabs)).append(type).append(" (").append(statement).append(") {\n");
        for (Element element:nestedElements) {
            if (element instanceof FragmentOrStaticImportCallElement fragmentOrStaticImportCallElement &&
                    fragmentOrStaticImportCallElement.isFragment()) {
                String variableName = "fragmentCall" + StringUtils.remove(UUID.randomUUID().toString(), "-");
                fragmentCallElementWriter.append("\t\t").append("HtmlFormat ").append(variableName).append(" = ");
                fragmentCallElementWriter.append(fragmentOrStaticImportCallElement.getContentCallObject());
                fragmentCallElementWriter.append(";\n");
                fragmentCallElementWriter.append("\t\t").append(variableName).append(".setContentGroup(this.contentGroup);\n");
                fragmentCallElementWriter.append("\t\t").append("contentBuilder.append(").append(variableName).append(".getContentForSection(\"");
                fragmentCallElementWriter.append(sectionName).append("\", specialElementsUtil));\n");
            } else {
                fragmentCallElementWriter.append("\n").append(element.getFragmentOrStaticImportCallElementWriter(sectionName));
            }
        }
        fragmentCallElementWriter.append(StringUtils.repeat('\t', this.numTabs)).append("}\n");
        return fragmentCallElementWriter;
    }
}
