package tech.petrepopescu.flamewing.parser;

import tech.petrepopescu.flamewing.parser.elements.Element;
import tech.petrepopescu.flamewing.parser.elements.ImportElement;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static tech.petrepopescu.flamewing.parser.FlamewingParser.VIEWS_BASE_PACKAGE;

public abstract class FlamewingFileParser {
    protected final File file;
    protected final ElementFactory elementFactory;
    protected final String basePackage;
    protected final List<Element> imports = new ArrayList<>();
    private Boolean parsed = false;

    protected FlamewingFileParser(File file, String basePackage, ElementFactory elementFactory) {
        this.file = file;
        this.elementFactory = elementFactory;
        this.basePackage = basePackage;
    }

    public void parse() throws IOException {
        if (Boolean.TRUE.equals(parsed)) {
            return;
        }
        doParse();
        parsed = true;
    }

    protected abstract void doParse() throws IOException;
    public abstract String write();

    void addMandatoryImports(String fileName) {
        Element flamewingFormatImport = new ImportElement("tech.petrepopescu.flamewing.format.*");
        Element flamewingViewImport = new ImportElement("tech.petrepopescu.flamewing.views.View");
        Element flamewingStringEscapeUtilsImport = new ImportElement("tech.petrepopescu.flamewing.utils.StringEscapeUtils");
        Element flamewingSpecialImport = new ImportElement("tech.petrepopescu.flamewing.special.*");
        Element springImport = new ImportElement("org.springframework.web.servlet.support.ServletUriComponentsBuilder");
        Element mapImport = new ImportElement("java.util.Map");
        Element hashMapImport = new ImportElement("java.util.HashMap");
        Element functionImport = new ImportElement("java.util.function.Function");
        Element staticStringsImport = new ImportElement("static " + VIEWS_BASE_PACKAGE + ".StaticStrings.*");
        imports.add(flamewingFormatImport);
        imports.add(flamewingViewImport);
        imports.add(flamewingStringEscapeUtilsImport);
        imports.add(flamewingSpecialImport);
        imports.add(springImport);
        imports.add(staticStringsImport);
        imports.add(mapImport);
        imports.add(hashMapImport);
        imports.add(functionImport);
        for (Element importElement:imports) {
            importElement.parse(fileName);
        }
    }

    public String getBasePackage() {
        return basePackage;
    }
}
