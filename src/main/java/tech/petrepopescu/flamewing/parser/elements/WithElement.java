package tech.petrepopescu.flamewing.parser.elements;

import tech.petrepopescu.flamewing.utils.StringUtils;
import tech.petrepopescu.flamewing.parser.ElementFactory;

import java.util.ArrayList;
import java.util.List;

public class WithElement extends AbstractContainerElement {
    private List<String> statements;
    private boolean isNullSafe = false;

    public WithElement(List<String> lines, int lineIndex, ElementFactory elementFactory, String builderName) {
        super(lines, lineIndex, elementFactory, builderName);
    }

    @Override
    public int parse(String fileName) {
        String line = this.lines.get(this.lineNumber);
        this.isNullSafe = StringUtils.trim(line).startsWith("@with?");
        this.statements = extractStatements(line);
        if (this.isNullSafe) {
            this.elementFactory.enteringIfStatement();
        }
        parseContentInside(line, fileName);
        if (this.isNullSafe) {
            this.elementFactory.exitingIfStatement();
        }

        return this.lineNumber;
    }

    @Override
    public StringBuilder write() {
        this.contentBuilder.append(StringUtils.repeat('\t', this.numTabs)).append("{\n");
        List<String> varNames = new ArrayList<>();
        for (String statement : this.statements) {
            this.contentBuilder.append(StringUtils.repeat('\t', this.numTabs + 1))
                    .append("var ").append(statement.trim()).append(";\n");
            
            if (this.isNullSafe) {
                String varName = statement.trim().split("=")[0].trim();
                varNames.add(varName);
            }
        }

        if (this.isNullSafe) {
            this.contentBuilder.append(StringUtils.repeat('\t', this.numTabs + 1)).append("if (");
            for (int i = 0; i < varNames.size(); i++) {
                if (i > 0) this.contentBuilder.append(" && ");
                this.contentBuilder.append(varNames.get(i)).append(" != null");
            }
            this.contentBuilder.append(") {\n");
        }

        for (Element element : this.nestedElements) {
            if (this.isNullSafe) {
                element.tabs(this.numTabs + 2);
            }
            this.contentBuilder.append(element.write());
        }

        if (this.isNullSafe) {
            this.contentBuilder.append(StringUtils.repeat('\t', this.numTabs + 1)).append("}\n");
        }

        this.contentBuilder.append(StringUtils.repeat('\t', this.numTabs)).append("}\n");

        if (this.isNullSafe && nextElement != null) {
            this.contentBuilder.append(nextElement.write());
        }

        return this.contentBuilder;
    }

    private List<String> extractStatements(String line) {
        String content = StringUtils.substring(line, StringUtils.indexOf(line, '(') + 1, StringUtils.lastIndexOf(line, ')'));
        List<String> result = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        int parenCount = 0;
        boolean inQuotes = false;
        for (int index = 0; index < content.length(); index++) {
            char character = content.charAt(index);
            if (character == '\"') {
                inQuotes = !inQuotes;
            } else if (!inQuotes) {
                if (character == '(') parenCount++;
                else if (character == ')') parenCount--;
            }

            if (character == ',' && parenCount == 0 && !inQuotes) {
                result.add(current.toString().trim());
                current = new StringBuilder();
            } else {
                current.append(character);
            }
        }
        if (!current.isEmpty()) {
            result.add(current.toString().trim());
        }
        return result;
    }
}
