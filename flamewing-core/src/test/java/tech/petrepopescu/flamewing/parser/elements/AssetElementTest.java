package tech.petrepopescu.flamewing.parser.elements;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tech.petrepopescu.flamewing.parser.ElementFactory;
import tech.petrepopescu.utils.TestUtil;

import java.util.List;
import java.util.Set;

class AssetElementTest {
    @Test
    void simpleTest() {
        String line = "@asset.path(\"\\css\\mycss.css\")";
        AssetElement element = new AssetElement(List.of(line), 0, new ElementFactory(Set.of()), ElementFactory.DEFAULT_BUILDER_NAME);
        element.parse("");

        String expected = "\t\tcontentBuilder.append(ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString());\n" +
                "\t\tcontentBuilder.append(\"\\\\css\\\\mycss.css\");\n";

        Assertions.assertEquals(expected, element.write().toString());
    }

    @Test
    void testInsideHtml() {
        String line = "<link href=\"@asset.path(\"\\css\\mycss.css\")\" rel=\"stylesheet\" />";
        ElementFactory elementFactory = new ElementFactory(null);
        Element element = elementFactory.getElement(line);
        element.parse("");

        String expected = "\t\tcontentBuilder.append(STATIC_HTML_THISISUUID);\n" +
                "\t\tcontentBuilder.append(ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString());\n" +
                "\t\tcontentBuilder.append(\"\\\\css\\\\mycss.css\");\n" +
                "\t\tcontentBuilder.append(STATIC_HTML_THISISUUID);\n" +
                "\t\tcontentBuilder.append(STATIC_HTML_THISISUUID);\n";

        Assertions.assertEquals(expected, TestUtil.sanitizeResult(element.write().toString()));
    }
}
