package tech.petrepopescu.flamewing.parser.elements;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tech.petrepopescu.flamewing.parser.ElementFactory;
import tech.petrepopescu.flamewing.parser.VariableRegistry;
import tech.petrepopescu.utils.TestUtil;

import java.util.List;
import java.util.Set;

public class RouteElementTests {
    @Test
    void simpleRouteTest() {
        String line = "@routes.TestController.testElement(0, 1, 2)";
        RouteElement element = new RouteElement(List.of(line), 0, new ElementFactory(Set.of()), ElementFactory.DEFAULT_BUILDER_NAME);
        element.parse("");

        Assertions.assertEquals("contentBuilder.append(routes.TestController.testElement(0, 1, 2).path());", element.write().toString().trim());
    }

    @Test
    void routeParsingWithFunctionCallAsParameter() {
        String line = "@routes.TestController.testElement(myVar.getId().toString())";
        RouteElement element = new RouteElement(List.of(line), 0, new ElementFactory(Set.of()), ElementFactory.DEFAULT_BUILDER_NAME);
        element.parse("");

        Assertions.assertEquals("contentBuilder.append(routes.TestController.testElement(myVar.getId().toString()).path());", element.write().toString().trim());
    }

    @Test
    void routeInsideHrefTag() {
        String line = "<a href=\"@routes.TestController.testElement(myVar.getId().toString())\" class=\"test\">Link</a>";
        VariableRegistry.getInstance().add("", "myVar", "String");
        HtmlElement element = new HtmlElement(List.of(line), 0, new ElementFactory(Set.of()), ElementFactory.DEFAULT_BUILDER_NAME);
        element.parse("");

        Assertions.assertEquals("contentBuilder.append(STATIC_HTML_THISISUUID);\n" +
                "\t\tcontentBuilder.append(routes.TestController.testElement(myVar.getId().toString()).path());\n" +
                "\t\tcontentBuilder.append(STATIC_HTML_THISISUUID);\n" +
                "\t\tcontentBuilder.append(STATIC_HTML_THISISUUID);\n", TestUtil.sanitizeResult(element.write().toString().trim()));
    }

    @Test
    void routeWithNullInsideHrefTag() {
        String line = "<a href=\"@routes.TestController.testElement(null)\" class=\"test\">Link</a>";
        HtmlElement element = new HtmlElement(List.of(line), 0, new ElementFactory(Set.of()), ElementFactory.DEFAULT_BUILDER_NAME);
        element.parse("");

        Assertions.assertEquals("contentBuilder.append(STATIC_HTML_THISISUUID);\n" +
                "\t\tcontentBuilder.append(routes.TestController.testElement(null).path());\n" +
                "\t\tcontentBuilder.append(STATIC_HTML_THISISUUID);\n" +
                "\t\tcontentBuilder.append(STATIC_HTML_THISISUUID);\n", TestUtil.sanitizeResult(element.write().toString().trim()));
    }
}
