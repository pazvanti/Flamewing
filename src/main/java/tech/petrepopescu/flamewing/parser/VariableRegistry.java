package tech.petrepopescu.flamewing.parser;

import tech.petrepopescu.flamewing.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class VariableRegistry {
    private final Map<String, Map<String, String>> argumentsByFileAndType;
    private final Map<String, String> staticString = new HashMap<>();
    private static final VariableRegistry INSTANCE = new VariableRegistry();
    private VariableRegistry() {
        argumentsByFileAndType  = new HashMap<>();
    }

    public static VariableRegistry getInstance() {
        return INSTANCE;
    }

    public void add(String fileName, String variableName, String variableType) {
        Map<String, String> variablesForFile = argumentsByFileAndType.get(fileName);
        if (variablesForFile == null) {
            variablesForFile = new HashMap<>();
            argumentsByFileAndType.put(fileName, variablesForFile);
        }
        variablesForFile.put(variableName, variableType);
    }

    public String getOrDefineStaticString(String value) {
        for (Map.Entry<String, String> variable : staticString.entrySet()) {
            if (variable.getValue().equals(value)) {
                return variable.getKey();
            }
        }

        String variableName = "STATIC_HTML_" + StringUtils.replace(UUID.randomUUID().toString(), "-", "");
        staticString.put(variableName, value);
        return variableName;
    }

    public Map<String, String> getStaticStrings() {
        return staticString;
    }

    public String getStaticString(String variableName) {
        return staticString.get(variableName);
    }

    public String getType(String fileName, String variableName) {
        Map<String, String> variablesForFile = argumentsByFileAndType.get(fileName);
        if (variablesForFile == null) {
            return null;
        }
        return variablesForFile.get(variableName);
    }
}
