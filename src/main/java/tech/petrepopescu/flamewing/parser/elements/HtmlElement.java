package tech.petrepopescu.flamewing.parser.elements;

import tech.petrepopescu.flamewing.parser.ElementFactory;
import tech.petrepopescu.flamewing.parser.VariableRegistry;
import tech.petrepopescu.flamewing.utils.StringEscapeUtils;
import tech.petrepopescu.flamewing.utils.StringUtils;

import java.util.List;

public class HtmlElement extends Element {
    public static final String TEST_ENV_PROPERTY = "flamewing.is.unit.test";

    private String variableName;
    private String actualCode;
    public HtmlElement(List<String> lines, int lineNumber, ElementFactory elementFactory, String builderName) {
        super(lines, lineNumber, elementFactory, builderName);
    }

    @Override
    public int parse(String fileName) {
        String line = this.lines.get(lineNumber);
        if (StringUtils.contains(line, '@')) {
            int indexOfAt = StringUtils.indexOf(line, '@');
            String untilAt = StringUtils.substring(line, 0, indexOfAt);
            actualCode = untilAt;
            variableName = VariableRegistry.getInstance().getOrDefineStaticString(StringEscapeUtils.escapeJava(actualCode));
            discoverNextElement(StringUtils.substring(line, indexOfAt), fileName);
        } else {
            actualCode = line;
            variableName = VariableRegistry.getInstance().getOrDefineStaticString(StringEscapeUtils.escapeJava(line));
        }
        return this.lineNumber;
    }

    @Override
    public StringBuilder write() {
        boolean isDebug = java.lang.management.ManagementFactory.getRuntimeMXBean().
                getInputArguments().toString().contains("-agentlib:jdwp");
        if (isDebug || Boolean.getBoolean(TEST_ENV_PROPERTY)) {
            // For debug purposes, we write the actual line. The code is not optimized, but we
            // can more easily see the generated code
            appendAsStringWithContentBuilder(actualCode);
        } else {
            appendWithContentBuilder(variableName);
        }
        if (this.nextElement != null) {
            this.contentBuilder.append(this.nextElement.write());
        } else {
            if (isDebug || Boolean.getBoolean(TEST_ENV_PROPERTY)) {
                appendAsStringWithContentBuilder("\n");
            } else {
                appendWithContentBuilder(VariableRegistry.getInstance().getOrDefineStaticString(StringEscapeUtils.escapeJava("\n")));
            }
        }
        return this.contentBuilder;
    }

    public boolean hasNextElement() {
        return this.nextElement != null;
    }

    public String getVariableName() {
        return this.variableName;
    }

    public String getActualCode() {
        return this.actualCode;
    }
}
