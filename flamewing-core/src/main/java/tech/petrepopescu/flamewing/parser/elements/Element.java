package tech.petrepopescu.flamewing.parser.elements;

import tech.petrepopescu.flamewing.parser.ElementFactory;
import tech.petrepopescu.flamewing.utils.StringEscapeUtils;
import tech.petrepopescu.flamewing.utils.StringUtils;

import java.util.List;

public abstract class Element {
    public static final String DEFAULT_SECTION_NAME = "html";
    private static final List<Character> END_CHARACTERS = List.of('\n', '\r', ' ', '\'', '\"', '<', ';', '/', ']', ' ');

    protected final ElementFactory elementFactory;
    protected final List<String> lines;
    protected String builderName;
    protected int lineNumber;
    protected int numTabs = 2;
    protected StringBuilder contentBuilder = new StringBuilder();
    protected Element nextElement = null;

    protected Element(List<String> lines, int lineIndex, ElementFactory elementFactory, String builderName) {
        this.lines = lines;
        this.lineNumber = lineIndex;
        this.elementFactory = elementFactory;
        this.builderName = builderName;
    }

    protected void appendWithContentBuilder(String line) {
        appendWithContentBuilder(line, this.numTabs);
    }
    protected void appendWithContentBuilder(String line, int numTabs) {
        this.contentBuilder.append(StringUtils.repeat('\t', numTabs))
                .append(builderName)
                .append(".append(")
                .append(line)
                .append(");")
                .append("\n");
    }

    protected void appendAsStringWithContentBuilder(String line) {
        this.contentBuilder.append(StringUtils.repeat('\t', this.numTabs))
                .append(builderName)
                .append(".append(").append('\"')
                .append(StringEscapeUtils.escapeJava(line)).append('\"')
                .append(");")
                .append("\n");
    }

    protected void appendAsCode(String code) {
        appendAsCode(code, this.numTabs);
    }

    protected void appendAsCode(String code, int tabs) {
        this.contentBuilder.append(StringUtils.repeat('\t', tabs))
                .append(code);
    }

    protected void discoverNextElement(String line, String fileName) {
        if (!StringUtils.isEmpty(line)) {
            Element element = this.elementFactory.getElement(line);
            element.tabs(this.numTabs);
            element.parse(fileName);
            this.nextElement = element;
        }
    }

    protected int discoverNextElement(String line, List<String> lines, int lineNumber, String fileName) {
        if (!StringUtils.isEmpty(line)) {
            Element element = this.elementFactory.getElement(lines, lineNumber);
            element.tabs(this.numTabs);
            int newLineNumber = element.parse(fileName);
            this.nextElement = element;
            return newLineNumber;
        }
        return lineNumber;
    }


    protected int indexOfElementEnd(String line, int start) {
        return indexOfElementEnd(line, start, false);
    }

    protected int indexOfElementEnd(String partialLine, int start, boolean checkQuote) {
        return indexOfElementEnd(partialLine, start, checkQuote, END_CHARACTERS);
    }

    protected int indexOfElementEnd(String partialLine, int start, boolean checkQuote, List<Character> endCharacters) {
        int end = start;
        int countOpenParentheses = 0;
        int countEndParentheses = 0;
        int countQuote = 0;
        while (end < partialLine.length()) {
            char currentChar = partialLine.charAt(end);
            if (currentChar == '(') {
                countOpenParentheses++;
            }
            if (currentChar == ')') {
                countEndParentheses++;
            }
            if (currentChar == '\"') {
                countQuote++;
            }
            if (endCharacters.contains(currentChar) && countEndParentheses == countOpenParentheses) {
                if (currentChar == ')') {
                    // If we end on `)` we need to include it as well since it is the end of a function call
                    end++;
                }
                if (checkQuote) {
                    if (countQuote % 2 == 0) {
                        return end;
                    }
                } else {
                    return end;
                }
            }
            end++;
        }

        return end;
    }

    public String getSectionName() {
        return DEFAULT_SECTION_NAME;
    }

    public void tabs(int numTabs) {
        this.numTabs = numTabs;
    }

    public StringBuilder getFragmentOrStaticImportCallElementWriter(String sectionName) {
        if (this.nextElement == null || sectionName == null) {
            return new StringBuilder();
        }

        if (nextElement instanceof FragmentOrStaticImportCallElement fragmentOrStaticImportCallElement) {
            StringBuilder fragmentCallElementWriter = new StringBuilder();
            if (fragmentOrStaticImportCallElement.isFragment()) {
                fragmentCallElementWriter.append("\n").append(fragmentOrStaticImportCallElement.getContentCallObject());
            }
            return fragmentCallElementWriter;
        }

        return nextElement.getFragmentOrStaticImportCallElementWriter(sectionName);
    }

    public void setBuilderName(String builderName) {
        this.builderName = builderName;
        if (this.nextElement != null) {
            this.nextElement.setBuilderName(builderName);
        }
    }

    /**
     * Parses the given element
     * @param fileName The base package and the file name which this element belongs to
     * @return The next line number
     */
    public abstract int parse(String fileName);

    public abstract StringBuilder write();

    public String getBuilderName() {
        return this.builderName;
    }
}
