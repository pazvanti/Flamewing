package tech.petrepopescu.flamewing.utils;

public class VariableDeclaration {
    private final String name;
    private final String type;
    private final String initialValue;
    private final boolean isStatic;

    public VariableDeclaration(String name, String type, String initialValue) {
        this.name = name;
        this.type = type;
        this.initialValue = initialValue;
        this.isStatic = false;
    }

    public VariableDeclaration(String name, String type, String initialValue, boolean isStatic) {
        this.name = name;
        this.type = type;
        this.initialValue = initialValue;
        this.isStatic = isStatic;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getInitialValue() {
        return initialValue;
    }

    public boolean isStatic() {
        return isStatic;
    }
}
