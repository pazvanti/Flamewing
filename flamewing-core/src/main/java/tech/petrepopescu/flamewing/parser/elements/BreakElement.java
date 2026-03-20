package tech.petrepopescu.flamewing.parser.elements;

import org.springframework.stereotype.Component;
import tech.petrepopescu.flamewing.parser.ElementFactory;

import java.util.List;

@Component
public class BreakElement extends Element {
    public BreakElement(List<String> lines, int lineIndex, ElementFactory elementFactory, String builderName) {
        super(lines, lineIndex, elementFactory, builderName);
    }

    @Override
    public int parse(String fileName) {
        return this.lineNumber;
    }

    @Override
    public StringBuilder write() {
        this.contentBuilder.append("break;\n");
        return this.contentBuilder;
    }
}
