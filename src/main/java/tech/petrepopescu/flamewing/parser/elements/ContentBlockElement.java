package tech.petrepopescu.flamewing.parser.elements;

import tech.petrepopescu.flamewing.utils.StringUtils;
import tech.petrepopescu.flamewing.parser.ElementFactory;

import java.util.List;

public class ContentBlockElement extends NestedElement {
    private String varName;
    public ContentBlockElement(List<String> fileLines, int lineIndex, ElementFactory elementFactory, String builderName) {
        super(fileLines, lineIndex, elementFactory, "", builderName);
    }

    @Override
    public int parse(String fileName) {
        String line = StringUtils.trim(this.lines.get(this.lineNumber));
        varName = StringUtils.trim(StringUtils.substring(line, 1, StringUtils.indexOf(line, '=')));
        String contentBuilderName = varName + "ContentBuilder";
        this.elementFactory.setBuilderName(contentBuilderName);
        this.numTabs++;
        parseContentInside(line, fileName);
        this.numTabs--;
        this.elementFactory.resetBuilderName();

        return this.lineNumber;
    }

    @Override
    public StringBuilder write() {
        this.contentBuilder.append(StringUtils.repeat('\t', this.numTabs))
                .append("FlamewingContent ").append(varName).append(" = new FlamewingContent() {\n");
        this.contentBuilder.append(StringUtils.repeat('\t', this.numTabs + 1)).append("public String render() {\n");
        this.contentBuilder.append(StringUtils.repeat('\t', this.numTabs + 2)).append("StringBuilder ")
                .append(varName).append("ContentBuilder = new StringBuilder();\n");
        for (Element element:this.nestedElements) {
            this.contentBuilder.append(element.write());
        }
        this.contentBuilder.append(StringUtils.repeat('\t', this.numTabs + 2)).append("return ")
                .append(varName).append("ContentBuilder.toString();\n");
        this.contentBuilder.append(StringUtils.repeat('\t', this.numTabs + 1)).append("}\n");
        this.contentBuilder.append(StringUtils.repeat('\t', this.numTabs)).append("};\n");

        return this.contentBuilder;
    }
}
