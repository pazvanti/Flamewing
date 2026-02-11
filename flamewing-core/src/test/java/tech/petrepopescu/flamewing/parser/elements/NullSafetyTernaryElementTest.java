package tech.petrepopescu.flamewing.parser.elements;

import tech.petrepopescu.flamewing.utils.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tech.petrepopescu.flamewing.parser.ElementFactory;
import tech.petrepopescu.utils.TestUtil;

import java.util.List;

class NullSafetyTernaryElementTest {
    @Test
    void stringAlternativeCorrectlyPrinted() {
        String line = "@myVar?:\"Alternative\"";

        NullSafetyTernaryElement element = new NullSafetyTernaryElement(List.of(line), 0, new ElementFactory(null), ElementFactory.DEFAULT_BUILDER_NAME);
        element.parse("");
        String expected = "\t\tvar tmpVar_THISISUUID = myVar;\n" +
                "\t\tif (tmpVar_THISISUUID != null) {\n" +
                "\t\t\tcontentBuilder.append(tmpVar_THISISUUID);\n" +
                "\t\t} else {\n" +
                "\t\t\tcontentBuilder.append(\"Alternative\");\n" +
                "\t\t}\n";

        String result = sanitizeResult(element.write().toString());
        Assertions.assertEquals(expected, result);
    }

    @Test
    void randomSpacesBetweenOperatorCorrectlyPrinted() {
        String line = "@myVar  ?:     \"Alternative\"";
        NullSafetyTernaryElement element = new NullSafetyTernaryElement(List.of(line), 0, new ElementFactory(null), ElementFactory.DEFAULT_BUILDER_NAME);
        element.parse("");
        String expected = "\t\tvar tmpVar_THISISUUID = myVar;\n" +
                "\t\tif (tmpVar_THISISUUID != null) {\n" +
                "\t\t\tcontentBuilder.append(tmpVar_THISISUUID);\n" +
                "\t\t} else {\n" +
                "\t\t\tcontentBuilder.append(\"Alternative\");\n" +
                "\t\t}\n";

        String result = sanitizeResult(element.write().toString());
        Assertions.assertEquals(expected, result);
    }

    @Test
    void alternativePrintedWhenMethodCall() {
        String line = "@myVar.myMethod()?:\"Alternative\"";
        NullSafetyTernaryElement element = new NullSafetyTernaryElement(List.of(line), 0, new ElementFactory(null), ElementFactory.DEFAULT_BUILDER_NAME);
        element.parse("");
        String expected = "\t\tvar tmpVar_THISISUUID = Stringifier.toString(myVar.myMethod());\n" +
                "\t\tif (tmpVar_THISISUUID != null) {\n" +
                "\t\t\tcontentBuilder.append(tmpVar_THISISUUID);\n" +
                "\t\t} else {\n" +
                "\t\t\tcontentBuilder.append(\"Alternative\");\n" +
                "\t\t}\n";

        String result = sanitizeResult(element.write().toString());
        Assertions.assertEquals(expected, result);
    }

    @Test
    void anotherVariableCorrectlyPrintedAsAlternative() {
        String line = "@myVar?:anotherVar";
        NullSafetyTernaryElement element = new NullSafetyTernaryElement(List.of(line), 0, new ElementFactory(null), ElementFactory.DEFAULT_BUILDER_NAME);
        element.parse("");
        String expected = "\t\tvar tmpVar_THISISUUID = myVar;\n" +
                "\t\tif (tmpVar_THISISUUID != null) {\n" +
                "\t\t\tcontentBuilder.append(tmpVar_THISISUUID);\n" +
                "\t\t} else {\n" +
                "\t\t\tcontentBuilder.append(anotherVar);\n" +
                "\t\t}\n";

        String result = sanitizeResult(element.write().toString());
        Assertions.assertEquals(expected, result);
    }

    @Test
    void ternaryInsideHtmlCorrectlyParsed() {
        String line = "<div class=\"@myVar?:\"Alternative\"\"></div>";
        ElementFactory elementFactory = new ElementFactory(null);
        Element element = elementFactory.getElement(line);
        element.parse("");
        String expected = "\t\tcontentBuilder.append(STATIC_HTML_THISISUUID);\n" +
                "\t\tvar tmpVar_THISISUUID = myVar;\n" +
                "\t\tif (tmpVar_THISISUUID != null) {\n" +
                "\t\t\tcontentBuilder.append(tmpVar_THISISUUID);\n" +
                "\t\t} else {\n" +
                "\t\t\tcontentBuilder.append(\"Alternative\");\n" +
                "\t\t}\n" +
                "\t\tcontentBuilder.append(STATIC_HTML_THISISUUID);\n" +
                "\t\tcontentBuilder.append(STATIC_HTML_THISISUUID);\n";

        String result = sanitizeResult(element.write().toString());
        Assertions.assertEquals(expected, TestUtil.sanitizeResult(result));
    }

    private String discoverUUID(String result) {
        String[] lines = StringUtils.split(result, "\n");
        for (String line:lines) {
            if (line.contains("var tmpVar_")) {
                return StringUtils.substringBetween(line, "var tmpVar_", " = ");
            }
        }
        return result;
    }

    private String sanitizeResult(String result) {
        String uuid = discoverUUID(result);
        return StringUtils.replace(result, uuid, "THISISUUID");
    }
}
