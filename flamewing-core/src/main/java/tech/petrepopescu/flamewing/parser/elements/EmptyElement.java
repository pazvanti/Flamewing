package tech.petrepopescu.flamewing.parser.elements;

import tech.petrepopescu.flamewing.parser.ElementFactory;

import java.util.List;

/**
 * This is an element that will generate an empty String.
 * It is being used so that we never return null from element builders
 */
public class EmptyElement extends Element{
    public EmptyElement(List<String> lines, int lineIndex, ElementFactory elementFactory, String builderName) {
        super(lines, lineIndex, elementFactory, builderName);
    }

    @Override
    public int parse(String fileName) {
        return this.lineNumber;
    }

    @Override
    public StringBuilder write() {
        return this.contentBuilder;
    }
}
