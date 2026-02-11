package tech.petrepopescu.flamewing.parser;

import tech.petrepopescu.flamewing.spring.config.FlamewingConfiguration;
import tech.petrepopescu.flamewing.utils.StringUtils;
import tech.petrepopescu.flamewing.exception.ParsingException;
import tech.petrepopescu.flamewing.parser.elements.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TemplateFile extends FlamewingFileParser {
    private final List<Element> lineElements = new ArrayList<>();
    private ConstructorElement constructorElement;
    private final StringBuilder builder = new StringBuilder();
    private final FlamewingConfiguration configuration;

    public TemplateFile(File file, String basePackage, ElementFactory elementFactory, FlamewingConfiguration configuration) {
        super(file, basePackage, elementFactory);
        this.configuration = configuration;
    }

    @Override
    protected void doParse() throws IOException {
        final List<String> lines = readLines(file, Charset.defaultCharset())
                .stream().filter(StringUtils::isNotBlank).toList();
        final String fileName = file.getName().substring(0, file.getName().indexOf(configuration.getViews().getExtension()));

        addMandatoryImports(fileName);

        int lineNumber = 0;
        int maxLines = lines.size();
        while (lineNumber < maxLines) {
            Element element = elementFactory.getElement(lines, lineNumber);
            if (element instanceof ImportElement) {
                imports.add(element);
            } else if(element instanceof ConstructorElement cElement) {
                cElement.setClassName(fileName);
                constructorElement = cElement;
            } else {
                lineElements.add(element);
            }
            lineNumber = element.parse(this.basePackage + "." + this.file.getName()) + 1;
        }

        if (constructorElement == null) {
            throw new ParsingException("No constructor defined for " + file.getName());
        }
    }

    private List<String> readLines(File file, Charset charset) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("File cannot be null");
        }

        Path path = file.toPath();
        return Files.readAllLines(path, charset);
    }

    public String className() {
        return this.constructorElement.getClassName();
    }

    @Override
    public String write() {
        if (!StringUtils.isEmpty(builder)) {
            return builder.toString();
        }

        builder.append("package ").append(basePackage).append(";\n\n");
        for (Element importElement:imports) {
            builder.append(importElement.write());
        }
        builder.append("\n");
        builder.append(this.constructorElement.write());

        List<FragmentOrStaticImportCallElement> fragmentCallElements = new ArrayList<>();
        fragmentCallElements.addAll(this.lineElements.stream()
                .filter(FragmentOrStaticImportCallElement.class::isInstance)
                .filter(element -> ((FragmentOrStaticImportCallElement) element).isFragment())
                .map(FragmentOrStaticImportCallElement.class::cast)
                .toList());

        List<Element> insertAtElements = this.lineElements.stream()
                .filter(InsertAtElement.class::isInstance)
                .toList();

        builder.append("\n");
        builder.append("\tprivate void populateSectionCalls() {\n");
        builder.append("\t\tcontentBySections.put(\"html\", this::getContentForhtml);\n");
        for (Element element : insertAtElements) {
            InsertAtElement insertAtElement = (InsertAtElement) element;
            builder.append("\t\tcontentBySections.put(\"").append(insertAtElement.getSectionName()).append("\", this::getContentFor")
                    .append(insertAtElement.getSectionName()).append(");\n");
        }
        builder.append("\t}\n");

        builder.append("\tpublic String getContent(FlamewingSpecialElementsUtil specialElementsUtil) {\n");
        builder.append("\t\t").append(" return getContentForSection(\"html\", specialElementsUtil);\n");
        builder.append("\t}\n");

        builder.append("\tpublic String getContentForSection(String sectionName, FlamewingSpecialElementsUtil specialElementsUtil) {\n");
        builder.append("\t\tif (!contentBySections.containsKey(sectionName)) {\n");
        builder.append("\t\t\treturn \"\";\n");
        builder.append("\t\t}\n");
        builder.append("\t\treturn contentBySections.get(sectionName).apply(specialElementsUtil);\n");
        builder.append("\t}\n\n");

        List<Element> nonInsertAtElements = lineElements.stream()
                .filter(element -> !(element instanceof InsertAtElement)).toList();
        Map<String, List<InsertAtElement>> insertAtElementsGroups = insertAtElements.stream()
                .map(InsertAtElement.class::cast)
                .collect(Collectors.groupingBy(InsertAtElement::getSectionName));
        List<Element> htmlElements = new ArrayList<>(nonInsertAtElements);
        if (insertAtElementsGroups.containsKey("html")) {
            htmlElements.addAll(insertAtElementsGroups.get("html"));
        }
        buildContentCallsForFragments(fragmentCallElements);
        appendGetContentForSection("html", builder, htmlElements, fragmentCallElements);
        for (Map.Entry<String, List<InsertAtElement>> entry : insertAtElementsGroups.entrySet()) {
            if (entry.getKey().equals("html")) {
                continue;
            }
            appendGetContentForSection(entry.getKey(), builder, entry.getValue(), fragmentCallElements);
        }

        builder.append("}\n");

        return builder.toString();
    }

    private void buildContentCallsForFragments(List<FragmentOrStaticImportCallElement> fragmentCallElements) {
        for (FragmentOrStaticImportCallElement fragmentCallElement : fragmentCallElements) {
            if (fragmentCallElement.isFragment() && fragmentCallElement.getContentVariableName() != null) {
                builder.append("\tprivate FlamewingContent ").append(fragmentCallElement.getContentVariableName())
                        .append("() {\n");
                builder.append(fragmentCallElement.getNestedElementsContent());
                builder.append("\t\treturn ").append(fragmentCallElement.getContentVariableName()).append(";\n");
                builder.append("\t}\n");
            }
        }
    }

    private void appendGetContentForSection(String sectionName, StringBuilder builder, List<? extends Element> lineElements,
                                            List<FragmentOrStaticImportCallElement> fragmentCallElements) {
        builder.append("\tpublic String getContentFor").append(sectionName).append("(FlamewingSpecialElementsUtil specialElementsUtil) {\n");
        builder.append("\t\t").append("final StringBuilder contentBuilder = new StringBuilder();\n\n");
        for (Element element : lineElements) {
            if (element instanceof SectionElement sectionElement) {
                builder.append(
                        sectionElement.write(fragmentCallElements,
                                lineElements.stream().filter(NestedElement.class::isInstance).map(NestedElement.class::cast).toList()
                        )
                );
            } else {
                builder.append(element.write());
            }
        }
        builder.append("\t\treturn contentBuilder.toString();\n");
        builder.append("\t}\n");
    }
}
