package tech.petrepopescu.flamewing.parser.route;

public class RouteVariable {
    private String name;
    private String varName;
    private Class<?> varType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVarName() {
        return varName;
    }

    public void setVarName(String varName) {
        this.varName = varName;
    }

    public Class<?> getVarType() {
        return varType;
    }

    public void setVarType(Class<?> varType) {
        this.varType = varType;
    }

    public static RouteVariableBuilder builder() {
        return new RouteVariableBuilder();
    }

    static class RouteVariableBuilder {
        private String name;
        private String varName;
        private Class<?> varType;

        public RouteVariableBuilder name(String name) {
            this.name = name;
            return this;
        }

        public RouteVariableBuilder varName(String varName) {
            this.varName = varName;
            return this;
        }

        public RouteVariableBuilder varType(Class<?> varType) {
            this.varType = varType;
            return this;
        }

        public RouteVariable build() {
            RouteVariable routeVariable = new RouteVariable();
            routeVariable.name = this.name;
            routeVariable.varName = this.varName;
            routeVariable.varType = this.varType;

            return routeVariable;
        }
    }
}
