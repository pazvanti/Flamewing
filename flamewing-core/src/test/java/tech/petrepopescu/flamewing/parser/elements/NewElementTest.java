package tech.petrepopescu.flamewing.parser.elements;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tech.petrepopescu.flamewing.parser.ElementFactory;
import tech.petrepopescu.flamewing.parser.VariableRegistry;

import java.util.List;
import java.util.Set;

class NewElementTest {
    @Test
    void singleVariableString() {
        String line = "@new(String s = \"my string\")";
        NewElement element = new NewElement(List.of(line), 0, new ElementFactory(Set.of()), ElementFactory.DEFAULT_BUILDER_NAME);
        element.parse("testFile");

        String expected = "\t\tString s = \"my string\";\n";


        Assertions.assertEquals(expected, element.write().toString());
        
        // Verify registration
        Assertions.assertEquals("String", VariableRegistry.getInstance().getType("testFile", "s"));
    }

    @Test
    void singleVariableVar() {
        String line = "@new(var myVar = new MyClass(\"test\", 1))";
        NewElement element = new NewElement(List.of(line), 0, new ElementFactory(Set.of()), ElementFactory.DEFAULT_BUILDER_NAME);
        element.parse("testFile");

        String expected = "\t\tvar myVar = new MyClass(\"test\", 1);\n";
        Assertions.assertEquals(expected, element.write().toString());
        
        // var should not be registered as a specific type
        Assertions.assertNull(VariableRegistry.getInstance().getType("testFile", "myVar"));
    }

    @Test
    void arrayDeclaration() {
        String line = "@new(String[] houseNames = {\"Classic Rectangle\", \"Modern Cube\"})";
        NewElement element = new NewElement(List.of(line), 0, new ElementFactory(Set.of()), ElementFactory.DEFAULT_BUILDER_NAME);
        element.parse("testFile");

        String expected = "\t\tString[] houseNames = {\"Classic Rectangle\", \"Modern Cube\"};\n";
        Assertions.assertEquals(expected, element.write().toString());
        
        Assertions.assertEquals("String[]", VariableRegistry.getInstance().getType("testFile", "houseNames"));
    }

    @Test
    void complexDeclaration() {
        String line = "@new(List<String> list = project.getTags())";
        NewElement element = new NewElement(List.of(line), 0, new ElementFactory(Set.of()), ElementFactory.DEFAULT_BUILDER_NAME);
        element.parse("testFile");

        String expected = "\t\tList<String> list = project.getTags();\n";
        Assertions.assertEquals(expected, element.write().toString());

        Assertions.assertEquals("List<String>", VariableRegistry.getInstance().getType("testFile", "list"));
    }
}
