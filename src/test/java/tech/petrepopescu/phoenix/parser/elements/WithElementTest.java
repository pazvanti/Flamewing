package tech.petrepopescu.phoenix.parser.elements;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tech.petrepopescu.phoenix.parser.ElementFactory;

import java.util.Collections;
import java.util.List;

class WithElementTest {
    @Test
    void singleVariable() {
        String line = "@with (x = 1) {";
        WithElement element = new WithElement(List.of(line, "}"), 0, new ElementFactory(Collections.emptySet()), ElementFactory.DEFAULT_BUILDER_NAME);
        element.parse("");

        String expected = "\t\t{\n" +
                "\t\t\tvar x = 1;\n" +
                "\t\t}\n";

        Assertions.assertEquals(expected, element.write().toString());
    }

    @Test
    void multipleVariables() {
        String line = "@with (x = 1, y = 2) {";
        WithElement element = new WithElement(List.of(line, "}"), 0, new ElementFactory(Collections.emptySet()), ElementFactory.DEFAULT_BUILDER_NAME);
        element.parse("");

        String expected = "\t\t{\n" +
                "\t\t\tvar x = 1;\n" +
                "\t\t\tvar y = 2;\n" +
                "\t\t}\n";

        Assertions.assertEquals(expected, element.write().toString());
    }

    @Test
    void multipleVariablesWithMethodCalls() {
        String line = "@with (x = list.get(0), y = map.get(\"key\").subList(0,3)) {";
        WithElement element = new WithElement(List.of(line, "}"), 0, new ElementFactory(Collections.emptySet()), ElementFactory.DEFAULT_BUILDER_NAME);
        element.parse("");

        String expected = "\t\t{\n" +
                "\t\t\tvar x = list.get(0);\n" +
                "\t\t\tvar y = map.get(\"key\").subList(0,3);\n" +
                "\t\t}\n";

        Assertions.assertEquals(expected, element.write().toString());
    }

    @Test
    void nullSafeWith() {
        String line = "@with? (s = list.get(0)) {";
        WithElement element = new WithElement(List.of(line, "}"), 0, new ElementFactory(Collections.emptySet()), ElementFactory.DEFAULT_BUILDER_NAME);
        element.parse("");

        String expected = "\t\t{\n" +
                "\t\t\tvar s = list.get(0);\n" +
                "\t\t\tif (s != null) {\n" +
                "\t\t\t}\n" +
                "\t\t}\n";

        Assertions.assertEquals(expected, element.write().toString());
    }

    @Test
    void nullSafeWithMultipleVariables() {
        String line = "@with? (s1 = list.get(0), s2 = map.get(\"key\")) {";
        WithElement element = new WithElement(List.of(line, "}"), 0, new ElementFactory(Collections.emptySet()), ElementFactory.DEFAULT_BUILDER_NAME);
        element.parse("");

        String expected = "\t\t{\n" +
                "\t\t\tvar s1 = list.get(0);\n" +
                "\t\t\tvar s2 = map.get(\"key\");\n" +
                "\t\t\tif (s1 != null && s2 != null) {\n" +
                "\t\t\t}\n" +
                "\t\t}\n";

        Assertions.assertEquals(expected, element.write().toString());
    }

    @Test
    void nullSafeWithElse() {
        String line = "@with? (s = list.get(0)) {";
        String elseLine = "} else {";
        ElementFactory elementFactory = new ElementFactory(Collections.emptySet());
        WithElement element = new WithElement(List.of(line, elseLine, "None!", "}"), 0, elementFactory, ElementFactory.DEFAULT_BUILDER_NAME);
        element.parse("");

        String expected = "\t\t{\n" +
                "\t\t\tvar s = list.get(0);\n" +
                "\t\t\tif (s != null) {\n" +
                "\t\t\t}\n" +
                "\t\t}\n" +
                " else {\n" +
                "\t\t\tcontentBuilder.append(STATIC_HTML_THISISUUID);\n" +
                "\t\t\tcontentBuilder.append(STATIC_HTML_THISISUUID);\n" +
                "\t\t}\n";

        Assertions.assertEquals(expected, tech.petrepopescu.utils.TestUtil.sanitizeResult(element.write().toString()));
    }
}
