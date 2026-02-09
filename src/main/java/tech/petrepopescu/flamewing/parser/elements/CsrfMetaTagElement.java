package tech.petrepopescu.flamewing.parser.elements;

import tech.petrepopescu.flamewing.utils.StringUtils;
import tech.petrepopescu.flamewing.parser.ElementFactory;

import java.util.List;

public class CsrfMetaTagElement extends Element {
    private boolean includeHeaderName = true;
    private String startingWhitespaces = "";
    public CsrfMetaTagElement(List<String> lines, int lineIndex, ElementFactory elementFactory, String builderName) {
        super(lines, lineIndex, elementFactory, builderName);
    }

    @Override
    public int parse(String fileName) {
        String line = this.lines.get(lineNumber);
        int indexOfTagStart = StringUtils.indexOf(line, "@csrf.meta(");
        int idxOfParamStart = indexOfTagStart + "@csrf.meta(".length();
        startingWhitespaces = StringUtils.substring(line, 0, indexOfTagStart);
        if (line.length() > idxOfParamStart + 1) {
            String param = StringUtils.substring(line, idxOfParamStart, StringUtils.lastIndexOf(line, ")"));
            this.includeHeaderName = Boolean.parseBoolean(param.trim());
        }
        return this.lineNumber;
    }

    @Override
    public StringBuilder write() {
        appendAsStringWithContentBuilder(startingWhitespaces);
        appendAsCode("var token = specialElementsUtil.getCsrfToken();\n");
        appendAsStringWithContentBuilder("<meta name=\"_csrf\" content=\"");
        appendWithContentBuilder("specialElementsUtil.getCsrfTokenValue(token)");
        appendAsStringWithContentBuilder("\"/>");
        if (includeHeaderName) {
            appendAsStringWithContentBuilder("\n");
            appendAsStringWithContentBuilder(startingWhitespaces);
            appendAsStringWithContentBuilder("<meta name=\"_csrf_header\" content=\"");
            appendWithContentBuilder("specialElementsUtil.getCsrfHeaderName(token)");
            appendAsStringWithContentBuilder("\"/>");
        }
        appendAsStringWithContentBuilder("\n");
        return this.contentBuilder;
    }
}
