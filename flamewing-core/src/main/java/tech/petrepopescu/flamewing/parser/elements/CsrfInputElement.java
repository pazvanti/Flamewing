package tech.petrepopescu.flamewing.parser.elements;

import tech.petrepopescu.flamewing.utils.StringUtils;
import tech.petrepopescu.flamewing.parser.ElementFactory;

import java.util.List;
import java.util.UUID;

public class CsrfInputElement extends Element {
    private String startingWhitespaces = "";
    public CsrfInputElement(List<String> lines, int lineIndex, ElementFactory elementFactory, String builderName) {
        super(lines, lineIndex, elementFactory, builderName);
    }

    @Override
    public int parse(String fileName) {
        startingWhitespaces = StringUtils.substring(this.lines.get(this.lineNumber),
                0,
                StringUtils.indexOf(this.lines.get(this.lineNumber), "@csrf.input()"));
        return this.lineNumber;
    }

    @Override
    public StringBuilder write() {
        appendAsStringWithContentBuilder(startingWhitespaces);
        String variableName = UUID.randomUUID().toString().replaceAll("-", "");
        appendAsCode("var token" + variableName + " = specialElementsUtil.getCsrfToken();\n");
        appendAsStringWithContentBuilder("<input type=\"hidden\" name=\"");
        appendWithContentBuilder("specialElementsUtil.getCsrfTokenName(token" + variableName + ")");
        appendAsStringWithContentBuilder("\" value=\"");
        appendWithContentBuilder("specialElementsUtil.getCsrfTokenValue(token" + variableName + ")");
        appendAsStringWithContentBuilder("\"/>\n");
        return this.contentBuilder;
    }
}
