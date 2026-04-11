package tech.petrepopescu.flamewing.parser.elements;

import tech.petrepopescu.flamewing.utils.StringUtils;
import tech.petrepopescu.flamewing.parser.ElementFactory;
import tech.petrepopescu.flamewing.parser.VariableRegistry;

import java.util.List;

public class NewElement extends Element {
    private String statement;

    public NewElement(List<String> lines, int lineIndex, ElementFactory elementFactory, String builderName) {
        super(lines, lineIndex, elementFactory, builderName);
    }

    @Override
    public int parse(String fileName) {
        String line = this.lines.get(this.lineNumber);
        int start = StringUtils.indexOf(line, "(");
        int end = indexOfElementEnd(line, start);
        this.statement = StringUtils.substring(line, start + 1, end - 1).trim();
        
        registerVariable(this.statement, fileName);
        
        if (end < line.length()) {
            discoverNextElement(StringUtils.substring(line, end), fileName);
        }
        
        return this.lineNumber;
    }

    private void registerVariable(String statement, String fileName) {
        String trimmed = statement.trim();
        int firstSpace = trimmed.indexOf(' ');
        int firstEquals = trimmed.indexOf('=');
        if (firstSpace != -1 && firstEquals != -1 && firstSpace < firstEquals) {
            String type = trimmed.substring(0, firstSpace).trim();
            String name = trimmed.substring(firstSpace, firstEquals).trim();
            if (!"var".equals(type)) {
                VariableRegistry.getInstance().add(fileName, name, type);
            }
        }
    }

    @Override
    public StringBuilder write() {
        this.contentBuilder.append(StringUtils.repeat('\t', this.numTabs))
                .append(this.statement)
                .append(";\n");
        
        if (this.nextElement != null) {
            this.contentBuilder.append(this.nextElement.write());
        }
        
        return this.contentBuilder;
    }
}
