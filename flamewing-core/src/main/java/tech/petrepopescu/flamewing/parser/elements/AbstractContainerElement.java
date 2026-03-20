package tech.petrepopescu.flamewing.parser.elements;

import tech.petrepopescu.flamewing.utils.StringUtils;
import tech.petrepopescu.flamewing.parser.ElementFactory;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractContainerElement extends Element {
    final List<Element> nestedElements = new ArrayList<>();
    protected AbstractContainerElement(List<String> lines, int lineIndex, ElementFactory elementFactory, String builderName) {
        super(lines, lineIndex, elementFactory, builderName);
    }

    protected void parseContentInside(String line, String fileName) {
        int indexOfStatementEnd = indexOfStatementEnd(line);
        if (indexOfStatementEnd == -1) {
            // The nested statement does not end on the same line
            this.lineNumber++;
            line = StringUtils.trim(this.lines.get(this.lineNumber));
            int numOpenedBrackets = 1;
            while (!StringUtils.startsWith(line, "}") || numOpenedBrackets > 1) {
                Element subElement = elementFactory.getElement(lines, lineNumber);
                subElement.tabs(this.numTabs + 1);
                nestedElements.add(subElement);
                this.lineNumber = subElement.parse(fileName) + 1;
                line = StringUtils.trim(this.lines.get(this.lineNumber));
                numOpenedBrackets += countOccurrencesInHtml(subElement, "{");
                numOpenedBrackets -= countOccurrencesInHtml(subElement, "}");
            }
            if (line.length() > 1) {
                this.lineNumber = discoverNextElement(line, lines, this.lineNumber, fileName);
            }
        } else {
            // The IF statement end on the same line. Extract the content
            int indexOfContentStart = this.indexOfContentStart(line);
            String content = StringUtils.substring(line, indexOfContentStart + 1, indexOfStatementEnd);
            Element subElement = elementFactory.getElement(content);
            subElement.tabs(this.numTabs + 1);
            subElement.parse(fileName);
            nestedElements.add(subElement);
            if (indexOfStatementEnd + 1 < line.length()) {
                discoverNextElement(StringUtils.substring(line, indexOfStatementEnd + 1), fileName);
            }
        }
    }

    private int countOccurrencesInHtml(Element element, String searchFor) {
        int count = 0;
        if (element instanceof HtmlElement htmlElement) {
            count += StringUtils.countMatches(htmlElement.getActualCode(), searchFor);
        }
        if (element.nextElement != null) {
            count += countOccurrencesInHtml(element.nextElement, searchFor);
        }

        return count;
    }

    private int indexOfStatementEnd(String line) {
        return StringUtils.indexOf(line, '}');
    }

    private int indexOfContentStart(String line) {
        return StringUtils.indexOf(line, '{');
    }

    @Override
    public void setBuilderName(String builderName) {
        this.builderName = builderName;
        for (Element nestedElement : nestedElements) {
            nestedElement.setBuilderName(builderName);
        }
    }

    @Override
    public StringBuilder getFragmentOrStaticImportCallElementWriter(String sectionName) {
        StringBuilder fragmentCallElements = super.getFragmentOrStaticImportCallElementWriter(sectionName);
        for (Element nestedElement : nestedElements) {
            if (nestedElement instanceof FragmentOrStaticImportCallElement fragmentOrStaticImportCallElement &&
                    fragmentOrStaticImportCallElement.isFragment()) {
                fragmentCallElements.append("\n").append(fragmentOrStaticImportCallElement.getContentCallObject());
            } else {
                fragmentCallElements.append("\n").append(nestedElement.getFragmentOrStaticImportCallElementWriter(sectionName));
            }
        }

        return fragmentCallElements;
    }
}
