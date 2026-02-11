package tech.petrepopescu.flamewing.parser.elements;

import tech.petrepopescu.flamewing.utils.StringUtils;
import tech.petrepopescu.flamewing.parser.ElementFactory;

import java.util.List;

public class CommentElement extends Element {
    public CommentElement(List<String> lines, int lineIndex, ElementFactory elementFactory, String builderName) {
        super(lines, lineIndex, elementFactory, builderName);
    }

    @Override
    public int parse(String fileName) {
        String line;
        do {
            line = this.lines.get(lineNumber);
            this.lineNumber++;
        } while (!StringUtils.contains(line, "*@"));
        int indexOfCommentEnd = StringUtils.indexOf(line, "*@");
        discoverNextElement(StringUtils.substring(line, indexOfCommentEnd + 2), fileName);
        return this.lineNumber - 1;
    }

    @Override
    public StringBuilder write() {
        if (this.nextElement != null) {
            this.contentBuilder.append(this.nextElement.write());
        }
        return this.contentBuilder;
    }
}
