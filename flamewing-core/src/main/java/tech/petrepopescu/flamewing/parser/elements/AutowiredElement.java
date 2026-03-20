package tech.petrepopescu.flamewing.parser.elements;

import tech.petrepopescu.flamewing.utils.StringUtils;
import tech.petrepopescu.flamewing.parser.ElementFactory;

import java.util.List;

public class AutowiredElement extends Element {
    private String className;
    private String variableName;
    public AutowiredElement(List<String> lines, int lineIndex, ElementFactory elementFactory, String builderName) {
        super(lines, lineIndex, elementFactory, builderName);
    }

    @Override
    public int parse(String fileName) {
        String line = this.lines.get(this.lineNumber);
        int indexOfVariableEnd = indexOfElementEnd(line, StringUtils.indexOf(line, "("), true, List.of(')'));
        String declaration = StringUtils.substring(line, "@autowired(".length(), indexOfVariableEnd - 1);
        this.className = StringUtils.substring(declaration, 0, StringUtils.indexOf(declaration, ' '));
        this.variableName = StringUtils.substring(declaration, StringUtils.indexOf(declaration, ' ') + 1);
        return this.lineNumber;
    }

    @Override
    public StringBuilder write() {
        appendAsCode(this.className + " " + this.variableName + " = specialElementsUtil.getAutowiredObject(" + this.className + ".class);\n");
        return this.contentBuilder;
    }
}
