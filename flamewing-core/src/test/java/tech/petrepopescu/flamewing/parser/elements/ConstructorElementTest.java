package tech.petrepopescu.flamewing.parser.elements;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tech.petrepopescu.flamewing.parser.ElementFactory;

import java.util.List;
import java.util.Set;

class ConstructorElementTest {
    @Test
    void testConstructorWithNoArguments() {
        final String line = "@args()";
        ConstructorElement element = new ConstructorElement(List.of(line), 0, new ElementFactory(Set.of()), ElementFactory.DEFAULT_BUILDER_NAME);
        element.setClassName("testClass");
        element.parse("");

        final String expected = "public final class testClass extends HtmlFormat {\n" +
                "\tprivate final Map<String, Function<FlamewingSpecialElementsUtil, String>> contentBySections = new HashMap<>();\n\n" +
                "\tprivate testClass() {\n" +
                "\t\tpopulateSectionCalls();\n" +
                "\t}\n" +
                "\tpublic static Format render() {\n" +
                "\t\treturn new testClass();\n" +
                "\t}\n";
        Assertions.assertEquals(expected, element.write().toString());
    }

    @Test
    void testConstructorWithNoArgumentsButSpace() {
        final String line = "@args ()";
        ConstructorElement element = new ConstructorElement(List.of(line), 0, new ElementFactory(Set.of()), ElementFactory.DEFAULT_BUILDER_NAME);
        element.setClassName("testClass");
        element.parse("");

        final String expected = "public final class testClass extends HtmlFormat {\n" +
                "\tprivate final Map<String, Function<FlamewingSpecialElementsUtil, String>> contentBySections = new HashMap<>();\n\n" +
                "\tprivate testClass() {\n" +
                "\t\tpopulateSectionCalls();\n" +
                "\t}\n" +
                "\tpublic static Format render() {\n" +
                "\t\treturn new testClass();\n" +
                "\t}\n";
        Assertions.assertEquals(expected, element.write().toString());
    }

    @Test
    void testConstructorWithArguments() {
        final String line = "@args(int a, String b)";
        ConstructorElement element = new ConstructorElement(List.of(line), 0, new ElementFactory(Set.of()), ElementFactory.DEFAULT_BUILDER_NAME);
        element.setClassName("testClass");
        element.parse("");

        final String expected = "public final class testClass extends HtmlFormat {\n" +
                "\tprivate final int a;\n" +
                "\tprivate final String b;\n" +
                "\tprivate final Map<String, Function<FlamewingSpecialElementsUtil, String>> contentBySections = new HashMap<>();\n\n" +
                "\tprivate testClass(int a, String b) {\n" +
                "\t\tthis.a = a;\n" +
                "\t\tthis.b = b;\n" +
                "\t\tpopulateSectionCalls();\n" +
                "\t}\n" +
                "\tpublic static Format render(int a, String b) {\n" +
                "\t\treturn new testClass(a, b);\n" +
                "\t}\n";
        Assertions.assertEquals(expected, element.write().toString());
    }

    @Test
    void testConstructorWithArgumentsAndSpace() {
        final String line = "@args  (int a, String b)";
        ConstructorElement element = new ConstructorElement(List.of(line), 0, new ElementFactory(Set.of()), ElementFactory.DEFAULT_BUILDER_NAME);
        element.setClassName("testClass");
        element.parse("");

        final String expected = "public final class testClass extends HtmlFormat {\n" +
                "\tprivate final int a;\n" +
                "\tprivate final String b;\n" +
                "\tprivate final Map<String, Function<FlamewingSpecialElementsUtil, String>> contentBySections = new HashMap<>();\n\n" +
                "\tprivate testClass(int a, String b) {\n" +
                "\t\tthis.a = a;\n" +
                "\t\tthis.b = b;\n" +
                "\t\tpopulateSectionCalls();\n" +
                "\t}\n" +
                "\tpublic static Format render(int a, String b) {\n" +
                "\t\treturn new testClass(a, b);\n" +
                "\t}\n";
        Assertions.assertEquals(expected, element.write().toString());
    }

    @Test
    void testConstructorWithArgumentsWithSimpleGenerics() {
        final String line = "@args(int a, List<String> b)";
        ConstructorElement element = new ConstructorElement(List.of(line), 0, new ElementFactory(Set.of()), ElementFactory.DEFAULT_BUILDER_NAME);
        element.setClassName("testClass");
        element.parse("");

        final String expected = "public final class testClass extends HtmlFormat {\n" +
                "\tprivate final int a;\n" +
                "\tprivate final List<String> b;\n" +
                "\tprivate final Map<String, Function<FlamewingSpecialElementsUtil, String>> contentBySections = new HashMap<>();\n\n" +
                "\tprivate testClass(int a, List<String> b) {\n" +
                "\t\tthis.a = a;\n" +
                "\t\tthis.b = b;\n" +
                "\t\tpopulateSectionCalls();\n" +
                "\t}\n" +
                "\tpublic static Format render(int a, List<String> b) {\n" +
                "\t\treturn new testClass(a, b);\n" +
                "\t}\n";
        Assertions.assertEquals(expected, element.write().toString());
    }

    @Test
    void testConstructorWithArgumentsWithGenerics() {
        final String line = "@args(int a, Pair<String, String> b)";
        ConstructorElement element = new ConstructorElement(List.of(line), 0, new ElementFactory(Set.of()), ElementFactory.DEFAULT_BUILDER_NAME);
        element.setClassName("testClass");
        element.parse("");

        final String expected = "public final class testClass extends HtmlFormat {\n" +
                "\tprivate final int a;\n" +
                "\tprivate final Pair<String, String> b;\n" +
                "\tprivate final Map<String, Function<FlamewingSpecialElementsUtil, String>> contentBySections = new HashMap<>();\n\n" +
                "\tprivate testClass(int a, Pair<String, String> b) {\n" +
                "\t\tthis.a = a;\n" +
                "\t\tthis.b = b;\n" +
                "\t\tpopulateSectionCalls();\n" +
                "\t}\n" +
                "\tpublic static Format render(int a, Pair<String, String> b) {\n" +
                "\t\treturn new testClass(a, b);\n" +
                "\t}\n";
        Assertions.assertEquals(expected, element.write().toString());
    }

    @Test
    void testConstructorWithArgumentsWithComplexGenerics() {
        final String line = "@args(int a, List<List<Pair<String, String>>> b)";
        ConstructorElement element = new ConstructorElement(List.of(line), 0, new ElementFactory(Set.of()), ElementFactory.DEFAULT_BUILDER_NAME);
        element.setClassName("testClass");
        element.parse("");

        final String expected = "public final class testClass extends HtmlFormat {\n" +
                "\tprivate final int a;\n" +
                "\tprivate final List<List<Pair<String, String>>> b;\n" +
                "\tprivate final Map<String, Function<FlamewingSpecialElementsUtil, String>> contentBySections = new HashMap<>();\n\n" +
                "\tprivate testClass(int a, List<List<Pair<String, String>>> b) {\n" +
                "\t\tthis.a = a;\n" +
                "\t\tthis.b = b;\n" +
                "\t\tpopulateSectionCalls();\n" +
                "\t}\n" +
                "\tpublic static Format render(int a, List<List<Pair<String, String>>> b) {\n" +
                "\t\treturn new testClass(a, b);\n" +
                "\t}\n";
        Assertions.assertEquals(expected, element.write().toString());
    }

    @Test
    void testConstructorWithArgumentsWithComplexGenericsWithOddSpacings() {
        final String line = "@args(int a, List<  List< Pair <String , String >  > > b)";
        ConstructorElement element = new ConstructorElement(List.of(line), 0, new ElementFactory(Set.of()), ElementFactory.DEFAULT_BUILDER_NAME);
        element.setClassName("testClass");
        element.parse("");

        final String expected = "public final class testClass extends HtmlFormat {\n" +
                "\tprivate final int a;\n" +
                "\tprivate final List< List< Pair <String , String > > > b;\n" +
                "\tprivate final Map<String, Function<FlamewingSpecialElementsUtil, String>> contentBySections = new HashMap<>();\n\n" +
                "\tprivate testClass(int a, List< List< Pair <String , String > > > b) {\n" +
                "\t\tthis.a = a;\n" +
                "\t\tthis.b = b;\n" +
                "\t\tpopulateSectionCalls();\n" +
                "\t}\n" +
                "\tpublic static Format render(int a, List< List< Pair <String , String > > > b) {\n" +
                "\t\treturn new testClass(a, b);\n" +
                "\t}\n";
        Assertions.assertEquals(expected, element.write().toString());
    }
}
