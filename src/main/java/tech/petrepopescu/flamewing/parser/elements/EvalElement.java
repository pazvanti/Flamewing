package tech.petrepopescu.flamewing.parser.elements;

import tech.petrepopescu.flamewing.utils.StringUtils;
import tech.petrepopescu.flamewing.exception.ParsingException;
import tech.petrepopescu.flamewing.parser.ElementFactory;

import java.util.List;

public class EvalElement extends Element {
    public EvalElement(List<String> lines, int lineIndex, ElementFactory elementFactory, String builderName) {
        super(lines, lineIndex, elementFactory, builderName);
    }

    @Override
    public int parse(String fileName) {
        String line = StringUtils.trim(this.lines.get(lineNumber));
        int idxStart = 1;
        int idxEnd = StringUtils.indexOf(line, '(') + 1;
        int opened = 1;
        int closed = 0;
        while (closed < opened) {
            if (idxEnd >= line.length()) {
                throw new ParsingException("Eval element not closed");
            }
            if (line.charAt(idxEnd) == '(') {
                opened++;
            } else if (line.charAt(idxEnd) == ')') {
                closed++;
            }
            idxEnd++;
        }

        String eval = StringUtils.substring(line, idxStart, idxEnd);
        appendWithContentBuilder(eval);
        discoverNextElement(StringUtils.substring(line, idxEnd), fileName);

        return this.lineNumber;
    }

    @Override
    public StringBuilder write() {
        if (this.nextElement != null) {
            this.contentBuilder.append(this.nextElement.write());
        }
        return this.contentBuilder;
    }
}
