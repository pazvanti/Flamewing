package tech.petrepopescu.flamewing.parser.elements;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tech.petrepopescu.flamewing.parser.ElementFactory;
import tech.petrepopescu.utils.TestUtil;

import java.util.List;
import java.util.Set;

class VariableElementTest {
    @Test
    void singleVariable() {
        String line = "@a";
        VariableElement element = new VariableElement(List.of(line), 0, new ElementFactory(Set.of()), ElementFactory.DEFAULT_BUILDER_NAME);
        element.parse("");

        String expected = "\t\tcontentBuilder.append(a);\n" +
                "\t\tcontentBuilder.append(\"\\n\");\n";

        Assertions.assertEquals(expected, element.write().toString());
    }

    @Test
    void variableMethodCall() {
        String line = "@a.b()";
        VariableElement element = new VariableElement(List.of(line), 0, new ElementFactory(Set.of()), ElementFactory.DEFAULT_BUILDER_NAME);
        element.parse("");

        String expected = "\t\tcontentBuilder.append(Stringifier.toString(a.b()));\n" +
                "\t\tcontentBuilder.append(\"\\n\");\n";

        Assertions.assertEquals(expected, element.write().toString());
    }

    @Test
    void variableMultipleMethodCalls() {
        String line = "@a.b().c()";
        VariableElement element = new VariableElement(List.of(line), 0, new ElementFactory(Set.of()), ElementFactory.DEFAULT_BUILDER_NAME);
        element.parse("");

        String expected = "\t\tcontentBuilder.append(Stringifier.toString(a.b().c()));\n" +
                "\t\tcontentBuilder.append(\"\\n\");\n";

        Assertions.assertEquals(expected, element.write().toString());
    }

    @Test
    void variableCallsVariableMethodCall() {
        String line = "@a.b.c()";
        VariableElement element = new VariableElement(List.of(line), 0, new ElementFactory(Set.of()), ElementFactory.DEFAULT_BUILDER_NAME);
        element.parse("");

        String expected = "\t\tcontentBuilder.append(Stringifier.toString(a.b.c()));\n" +
                "\t\tcontentBuilder.append(\"\\n\");\n";

        Assertions.assertEquals(expected, element.write().toString());
    }

    @Test
    void variableInsideUrlCall() {
        String line = "/user/myCode/snippet/@item.getId()/edit";
        Element element = new HtmlElement(List.of(line), 0, new ElementFactory(Set.of()), ElementFactory.DEFAULT_BUILDER_NAME);
        element.parse("");

        String expected = "\t\tcontentBuilder.append(STATIC_HTML_THISISUUID);\n" +
                "\t\tcontentBuilder.append(Stringifier.toString(item.getId()));\n" +
                "\t\tcontentBuilder.append(STATIC_HTML_THISISUUID);\n" +
                "\t\tcontentBuilder.append(STATIC_HTML_THISISUUID);\n";

        Assertions.assertEquals(expected, TestUtil.sanitizeResult(element.write().toString()));
    }
}
