package tech.petrepopescu.flamewing.parser.elements;

import tech.petrepopescu.flamewing.utils.StringUtils;
import tech.petrepopescu.flamewing.parser.ElementFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SectionElement extends AbstractContainerElement {
    private String sectionName = null;
    public SectionElement(List<String> lines, int lineIndex, ElementFactory elementFactory, String builderName) {
        super(lines, lineIndex, elementFactory, builderName);
    }

    @Override
    public int parse(String fileName) {
        String line = this.lines.get(this.lineNumber);
        sectionName = extractStatement(line);
        parseContentInside(line, fileName);
        return this.lineNumber;
    }

    @Override
    public StringBuilder write() {
        return write(List.of(), List.of());
    }

    public StringBuilder write(List<FragmentOrStaticImportCallElement> fragments, List<NestedElement> nestedElements) {
        for (Element element:this.nestedElements) {
            this.contentBuilder.append(element.write());
        }
        List<String> fragmentVariableNames = new ArrayList<>();
        for (int count = 0; count < fragments.size(); count++) {
            fragmentVariableNames.add("fragmentCall" + StringUtils.remove(UUID.randomUUID().toString(), '-'));
        }
        for (int count = 0; count < fragments.size(); count++) {
            FragmentOrStaticImportCallElement fragment = fragments.get(count);
            appendAsCode("HtmlFormat " + fragmentVariableNames.get(count) + " = ");
            appendAsCode(fragment.getContentCallObject().toString() + ";\n");
            appendAsCode(fragmentVariableNames.get(count) + ".setContentGroup(this.contentGroup);\n");
        }
        for (int count = 0; count < fragments.size(); count++) {
            appendAsCode("contentBuilder.append(" + fragmentVariableNames.get(count) + ".getContentForSection(\"" + this.sectionName + "\", specialElementsUtil));\n");
        }

        for (NestedElement nestedElement:nestedElements) {
            appendAsCode(nestedElement.getFragmentOrStaticImportCallElementWriter(this.sectionName).toString());
        }
        return this.contentBuilder;
    }

    @Override
    public String getSectionName() {
        if (StringUtils.isBlank(sectionName)) {
            return DEFAULT_SECTION_NAME;
        }
        return sectionName;
    }

    @Override
    public String getBuilderName() {
        if (StringUtils.isBlank(sectionName)) {
            return this.builderName;
        }
        return sectionName + "ContentBuilder";
    }


    private String extractStatement(String line) {
        return StringUtils.substring(line, StringUtils.indexOf(line, '(') + 2, StringUtils.lastIndexOf(line, ')') - 1);
    }
}
