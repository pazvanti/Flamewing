package tech.petrepopescu.flamewing.parser.route;

public class RouteVariable {
    private String name;
    private String varName;
    private Class<?> varType;
    private boolean required = true;


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

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }


    public static RouteVariableBuilder builder() {
        return new RouteVariableBuilder();
    }

    static class RouteVariableBuilder {
        private String name;
        private String varName;
        private Class<?> varType;
        private boolean required = true;


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

        public RouteVariableBuilder required(boolean required) {
            this.required = required;
            return this;
        }


        public RouteVariable build() {
            RouteVariable routeVariable = new RouteVariable();
            routeVariable.name = this.name;
            routeVariable.varName = this.varName;
            routeVariable.varType = this.varType;
            routeVariable.required = this.required;


            return routeVariable;
        }
    }
}
