package tech.petrepopescu.flamewing.parser.elements;

import tech.petrepopescu.flamewing.utils.StringUtils;
import tech.petrepopescu.flamewing.exception.ParsingException;
import tech.petrepopescu.flamewing.parser.ElementFactory;

import java.util.List;
import java.util.UUID;

public class FragmentOrStaticImportCallElement extends NestedElement {
    private boolean isFragment = true;
    private String fragmentName;
    private String contentVariableName = null;
    private int indexOfParamEnd;
    private int indexOfParamStart;
    private String parameters = null;
    public FragmentOrStaticImportCallElement(List<String> lines, int lineIndex, ElementFactory elementFactory, String builderName) {
        super(lines, lineIndex, elementFactory, "", builderName);
    }

    @Override
    public int parse(String fileName) {
        final String line = StringUtils.trim(this.lines.get(this.lineNumber));
        indexOfParamStart = StringUtils.indexOf(line, "(");
        indexOfParamEnd = findParameterEnd(line, indexOfParamStart);
        fragmentName = StringUtils.substring(line, 1, indexOfParamStart);
        if (elementFactory.isStaticImport(fragmentName)) {
            this.isFragment = false;
            return parseStaticImport(line, indexOfParamEnd, fileName);
        }
        return parseFragment(line);
    }

    private int findParameterEnd(String line, int parameterStart) {
        int openedBrackets = 0;
        int parameterEnd;
        for (parameterEnd = parameterStart + 1; parameterEnd < line.length(); parameterEnd++) {
            if (line.charAt(parameterEnd) == '(') {
                openedBrackets++;
            }
            if (line.charAt(parameterEnd) == ')') {
                if (openedBrackets == 0) return parameterEnd;
                openedBrackets--;
            }
        }

        throw new ParsingException("Could not parse fragment call: " + line);
    }

    private int parseStaticImport(String line, int indexOfParamEnd, String fileName) {
        appendWithContentBuilder(StringUtils.substring(line, 1, indexOfParamEnd + 1));
        this.contentBuilder.append("\n");
        discoverNextElement(StringUtils.substring(line, indexOfParamEnd + 1), fileName);
        return this.lineNumber;
    }

    private int parseFragment(String line) {
        int indexOfTemplateCall = StringUtils.indexOf(fragmentName, ".template");
        if (indexOfTemplateCall < 0) {
            // this is NOT a fragment call
            return this.lineNumber;
        }

        line = StringUtils.trim(line);
        if (StringUtils.endsWith(line ,"{")) {
            // We have fragment content
            contentVariableName = "content_" + StringUtils.remove(UUID.randomUUID().toString(), '-');
            String contentBuilderName = contentVariableName + "ContentBuilder";
            this.elementFactory.setBuilderName(contentBuilderName);
            parseContentInside(line, fragmentName);
            this.elementFactory.resetBuilderName();
        }
        if (indexOfParamStart + 1 != indexOfParamEnd) {
            parameters = StringUtils.substring(line, indexOfParamStart + 1, indexOfParamEnd);
        }
        fragmentName = StringUtils.substring(fragmentName, 0, indexOfTemplateCall);
        elementFactory.potentialFragmentCall(fragmentName);
        return this.lineNumber;
    }

    @Override
    public StringBuilder write() {
        if (isFragment) {
            this.writeContentForHtml();
        }
        if (this.nextElement != null) {
            this.contentBuilder.append(this.nextElement.write());
        }
        return this.contentBuilder;
    }

    private void writeContentForHtml() {
        StringBuilder fragmentCall = getContentCallForSection("html");
        appendWithContentBuilder(fragmentCall.toString());
    }

    public StringBuilder getNestedElementsContent() {
        StringBuilder contentBuilder = new StringBuilder();
        if (!this.nestedElements.isEmpty()) {
            contentBuilder.append(StringUtils.repeat('\t', this.numTabs))
                    .append("FlamewingContent ").append(contentVariableName).append(" = new FlamewingContent() {\n");
            contentBuilder.append(StringUtils.repeat('\t', this.numTabs + 1)).append("public String render() {\n");
            contentBuilder.append(StringUtils.repeat('\t', this.numTabs + 2)).append("StringBuilder ")
                    .append(contentVariableName).append("ContentBuilder = new StringBuilder();\n");
            for (Element element:this.nestedElements) {
                contentBuilder.append(element.write());
            }
            contentBuilder.append(StringUtils.repeat('\t', this.numTabs + 2)).append("return ")
                    .append(contentVariableName).append("ContentBuilder.toString();\n");
            contentBuilder.append(StringUtils.repeat('\t', this.numTabs + 1)).append("}\n");
            contentBuilder.append(StringUtils.repeat('\t', this.numTabs)).append("};\n");
        }

        return contentBuilder;
    }

    public StringBuilder getContentCallForSection(String sectionName) {
        StringBuilder call = new StringBuilder("View.of(\"" + fragmentName + "\"");
        if (indexOfParamStart + 1 == indexOfParamEnd) {
            // no arguments
            if (contentVariableName != null) {
                call.append(", ").append(contentVariableName + "()");
            }
            call.append(")");
        } else {
            call.append(", ").append(parameters);
            if (contentVariableName != null) {
                call.append(", ").append(contentVariableName + "()");
            }
            call.append(")");
        }
        call.append(".getContentForSection(\"").append(sectionName).append("\", specialElementsUtil)");

        return call;
    }

    public StringBuilder getContentCallObject() {
        StringBuilder call = new StringBuilder("View.of(\"" + fragmentName + "\"");
        if (indexOfParamStart + 1 == indexOfParamEnd) {
            // no arguments
            if (contentVariableName != null) {
                call.append(", ").append(contentVariableName + "()");
            }
            call.append(")");
        } else {
            call.append(", ").append(parameters);
            if (contentVariableName != null) {
                call.append(", ").append(contentVariableName + "()");
            }
            call.append(")");
        }
        return call;
    }

    public boolean isFragment() {
        return this.isFragment;
    }

    public String getContentVariableName() {
        return this.contentVariableName;
    }
}
