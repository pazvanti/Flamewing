package tech.petrepopescu.flamewing.parser.elements;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tech.petrepopescu.flamewing.parser.ElementFactory;
import tech.petrepopescu.flamewing.parser.VariableRegistry;
import tech.petrepopescu.utils.TestUtil;

import java.util.List;
import java.util.Set;

class HtmlElementTests {

    @BeforeAll
    static void setup() {
        System.setProperty(HtmlElement.TEST_ENV_PROPERTY, "true");
    }

    @AfterAll
    static void tearDown() {
        System.clearProperty(HtmlElement.TEST_ENV_PROPERTY);
    }

    @Test
    void testWithVariable() {
        String line = "<h6>@b</h6>";
        HtmlElement element = new HtmlElement(List.of(line), 0, new ElementFactory(Set.of()), ElementFactory.DEFAULT_BUILDER_NAME);
        VariableRegistry.getInstance().add("", "b", "String");
        element.parse("");

        String expected = "\t\tcontentBuilder.append(\"<h6>\");\n" +
                "\t\tcontentBuilder.append(StringEscapeUtils.escapeHtml(b));\n" +
                "\t\tcontentBuilder.append(\"</h6>\");\n" +
                "\t\tcontentBuilder.append(\"\\n\");\n";

        Assertions.assertEquals(expected, TestUtil.sanitizeResult(element.write().toString()));
    }

    @Test
    void testWithRawVariable() {
        String line = "<h6>@raw(b)</h6>";
        HtmlElement element = new HtmlElement(List.of(line), 0, new ElementFactory(Set.of()), ElementFactory.DEFAULT_BUILDER_NAME);
        VariableRegistry.getInstance().add("", "b", "String");
        element.parse("");

        String expected = "\t\tcontentBuilder.append(\"<h6>\");\n" +
                "\t\tcontentBuilder.append(b);\n" +
                "\t\tcontentBuilder.append(\"</h6>\");\n" +
                "\t\tcontentBuilder.append(\"\\n\");\n";

        Assertions.assertEquals(expected, TestUtil.sanitizeResult(element.write().toString()));
    }
    @Test
    void testWithRouteElement() {
        String line = "<a href=\"@routes.TestController.testElement(0, 1, 2)\">Link</a>";
        HtmlElement element = new HtmlElement(List.of(line), 0, new ElementFactory(Set.of()), ElementFactory.DEFAULT_BUILDER_NAME);
        element.parse("");

        String expected = "\t\tcontentBuilder.append(\"<a href=\\\"\");\n" +
                "\t\tcontentBuilder.append(routes.TestController.testElement(0, 1, 2).path());\n" +
                "\t\tcontentBuilder.append(\"\\\">Link</a>\");\n" +
                "\t\tcontentBuilder.append(\"\\n\");\n";

        Assertions.assertEquals(expected, TestUtil.sanitizeResult(element.write().toString()));
    }

    @Test
    void testWithEmail() {
        String line = "<a href=\"mailto:test@@test.com\">Email me</a>";
        HtmlElement element = new HtmlElement(List.of(line), 0, new ElementFactory(Set.of()), ElementFactory.DEFAULT_BUILDER_NAME);
        element.parse("");

        String expected = "\t\tcontentBuilder.append(\"<a href=\\\"mailto:test\");\n" +
                "\t\tcontentBuilder.append(\"@test.com\");\n" +
                "\t\tcontentBuilder.append(\"\\\">Email me</a>\");\n" +
                "\t\tcontentBuilder.append(\"\\n\");\n";

        Assertions.assertEquals(expected, TestUtil.sanitizeResult(element.write().toString()));
    }

    @Test
    void testIfInsideHtml() {
        String line = "<tr class=\"@if (i % 2 == 0) {odd} \">";
        HtmlElement element = new HtmlElement(List.of(line), 0, new ElementFactory(Set.of()), ElementFactory.DEFAULT_BUILDER_NAME);
        element.parse("");

        String expected = "\t\tcontentBuilder.append(\"<tr class=\\\"\");\n" +
                "\t\tif (i % 2 == 0) {\n" +
                "\t\t\tcontentBuilder.append(\"odd\");\n" +
                "\t\t\tcontentBuilder.append(\"\\n\");\n" +
                "\t\t}\n" +
                "\t\tcontentBuilder.append(\" \\\">\");\n" +
                "\t\tcontentBuilder.append(\"\\n\");\n";

        Assertions.assertEquals(expected, TestUtil.sanitizeResult(element.write().toString()));
    }

    @Test
    void testVariableInsideCss() {
        String line = "<input type=\"text\" placeholder=\"Value of a\" value=\"@a\" name=\"a-value\">";
        HtmlElement element = new HtmlElement(List.of(line), 0, new ElementFactory(Set.of()), ElementFactory.DEFAULT_BUILDER_NAME);
        element.parse("");

        String expected = "\t\tcontentBuilder.append(\"<input type=\\\"text\\\" placeholder=\\\"Value of a\\\" value=\\\"\");\n" +
                "\t\tcontentBuilder.append(a);\n" +
                "\t\tcontentBuilder.append(\"\\\" name=\\\"a-value\\\">\");\n" +
                "\t\tcontentBuilder.append(\"\\n\");\n";

        Assertions.assertEquals(expected, TestUtil.sanitizeResult(element.write().toString()));
    }

    @Test
    void testStreamConcatInsidejavascript() {
        String line = "<script>let tags = [@raw(myTags.stream().map(tag -> '\\\"' + tag.trim() + '\\\"').collect(Collectors.joining(\",\")))];</script>";
        HtmlElement element = new HtmlElement(List.of(line), 0, new ElementFactory(Set.of()), ElementFactory.DEFAULT_BUILDER_NAME);
        element.parse("");

        String expected = "\t\tcontentBuilder.append(\"<script>let tags = [\");\n" +
                "\t\tcontentBuilder.append(myTags.stream().map(tag -> '\\\"' + tag.trim() + '\\\"').collect(Collectors.joining(\",\")));\n" +
                "\t\tcontentBuilder.append(\"];</script>\");\n" +
                "\t\tcontentBuilder.append(\"\\n\");\n";

        Assertions.assertEquals(expected, TestUtil.sanitizeResult(element.write().toString()));
    }
}
