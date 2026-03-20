package tech.petrepopescu.flamewing.parser.elements;

import tech.petrepopescu.flamewing.utils.StringUtils;
import tech.petrepopescu.flamewing.parser.ElementFactory;

import java.util.List;

public class ImportElement extends Element {
    public ImportElement(List<String> lines, int lineIndex, ElementFactory elementFactory, String builderName) {
        super(lines, lineIndex, elementFactory, builderName);
    }

    public ImportElement(String toImport) {
        super(List.of("import " + toImport + ";"), 0, null, "contentBuilder");
    }

    @Override
    public int parse(String fileName) {
        String line = StringUtils.trim(this.lines.get(this.lineNumber));
        this.contentBuilder.append(StringUtils.replace(line, "@import ", "import "));
        if (!StringUtils.endsWith(line, ";")) {
            this.contentBuilder.append(";");
        }
        if (StringUtils.contains(line, " static ") && this.elementFactory != null) {
            this.elementFactory.addStaticImport(StringUtils.substring(line, StringUtils.lastIndexOf(line, ".") + 1));
        }
        return lineNumber;
    }

    @Override
    public StringBuilder write() {
        return this.contentBuilder.append("\n");
    }
}
