package tech.petrepopescu.flamewing.parser.compiler;

import tech.petrepopescu.flamewing.utils.StringUtils;

import javax.tools.SimpleJavaFileObject;
import java.net.URI;

public class SourceCodeObject extends SimpleJavaFileObject {
    private final String sourceCode;
    private final boolean templateFile;
    private final String fullClassName;
    private final String basePackage;


    public SourceCodeObject(String className, String sourceCode, String basePackage) {
        this(className, sourceCode, basePackage, false);
    }

    public SourceCodeObject(String className, String sourceCode, String basePackage, boolean templateFile) {
        super(URI.create("string:///" + className.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
        this.sourceCode = sourceCode;
        this.templateFile = templateFile;
        this.basePackage = basePackage;
        if (StringUtils.endsWith(basePackage, ".")) {
            fullClassName = basePackage  + className;
        } else {
            fullClassName = basePackage + "." + className;
        }
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        return sourceCode;
    }

    public boolean isTemplateFile() {
        return templateFile;
    }

    public String getFullClassName() {
        return fullClassName;
    }

    public String getContent() {
        return sourceCode;
    }

    public String getBasePackage() {
        return basePackage;
    }
}