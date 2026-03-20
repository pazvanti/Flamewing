package tech.petrepopescu.flamewing.parser.elements;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tech.petrepopescu.flamewing.parser.ElementFactory;

import java.util.List;

class RawElementTest {
    @Test
    void correctBuildingWhenSimpleVariable() {
        String line = "@raw(myVar)";
        RawElement element = new RawElement(List.of(line), 0, new ElementFactory(null), ElementFactory.DEFAULT_BUILDER_NAME);
        element.parse("");
        String expected = "\t\tcontentBuilder.append(myVar);\n";
        Assertions.assertEquals(expected, element.write().toString());
    }

    @Test
    void correctBuildingWhenVariableWithMethodCall() {
        String line = "@raw(myVar.myMethod())";
        RawElement element = new RawElement(List.of(line), 0, new ElementFactory(null), ElementFactory.DEFAULT_BUILDER_NAME);
        element.parse("");
        String expected = "\t\tcontentBuilder.append(myVar.myMethod());\n";
        Assertions.assertEquals(expected, element.write().toString());
    }

    @Test
    void correctBuildingWhenVariableWithMethodCallWithParams() {
        String line = "@raw(myVar.myMethod(var1, var2))";
        RawElement element = new RawElement(List.of(line), 0, new ElementFactory(null), ElementFactory.DEFAULT_BUILDER_NAME);
        element.parse("");
        String expected = "\t\tcontentBuilder.append(myVar.myMethod(var1, var2));\n";
        Assertions.assertEquals(expected, element.write().toString());
    }

    @Test
    void correctBuildingWhenVariableWithMultipleMethodCalls() {
        String line = "@raw(myVar.first().second().third())";
        RawElement element = new RawElement(List.of(line), 0, new ElementFactory(null), ElementFactory.DEFAULT_BUILDER_NAME);
        element.parse("");
        String expected = "\t\tcontentBuilder.append(myVar.first().second().third());\n";
        Assertions.assertEquals(expected, element.write().toString());
    }
}
