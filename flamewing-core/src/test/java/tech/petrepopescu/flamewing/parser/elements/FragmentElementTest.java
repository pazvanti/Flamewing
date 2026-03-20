package tech.petrepopescu.flamewing.parser.elements;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tech.petrepopescu.flamewing.parser.ElementFactory;

import java.util.List;
import java.util.Set;

class FragmentElementTest {

    @Test
    void simpleTest() {
        String line = "@fragments.myFragment.template()";

        FragmentOrStaticImportCallElement element = new FragmentOrStaticImportCallElement(List.of(line), 0,
                new ElementFactory(Set.of()), ElementFactory.DEFAULT_BUILDER_NAME);
        element.parse("");

        String expected = "\t\tcontentBuilder.append(View.of(\"fragments.myFragment\").getContentForSection(\"html\", specialElementsUtil));\n";
        Assertions.assertEquals(expected, element.write().toString());
    }

    @Test
    void testWithParameters() {
        String line = "@fragments.myFragment.template(1, 2)";

        FragmentOrStaticImportCallElement element = new FragmentOrStaticImportCallElement(List.of(line), 0,
                new ElementFactory(Set.of()), ElementFactory.DEFAULT_BUILDER_NAME);
        element.parse("");

        String expected = "\t\tcontentBuilder.append(View.of(\"fragments.myFragment\", 1, 2).getContentForSection(\"html\", specialElementsUtil));\n";
        Assertions.assertEquals(expected, element.write().toString());
    }

    @Test
    void testWithFunctionCallAsParameter() {
        String line = "@fragments.myFragment.template(myVar.getValue())";

        FragmentOrStaticImportCallElement element = new FragmentOrStaticImportCallElement(List.of(line), 0,
                new ElementFactory(Set.of()), ElementFactory.DEFAULT_BUILDER_NAME);
        element.parse("");

        String expected = "\t\tcontentBuilder.append(View.of(\"fragments.myFragment\", myVar.getValue()).getContentForSection(\"html\", specialElementsUtil));\n";
        Assertions.assertEquals(expected, element.write().toString());
    }

    @Test
    void testWithMultipleFunctionsCallAsParameter() {
        String line = "@fragments.myFragment.template(myVar.getValue(), getAString())";

        FragmentOrStaticImportCallElement element = new FragmentOrStaticImportCallElement(List.of(line), 0,
                new ElementFactory(Set.of()), ElementFactory.DEFAULT_BUILDER_NAME);
        element.parse("");

        String expected = "\t\tcontentBuilder.append(View.of(\"fragments.myFragment\", myVar.getValue(), getAString()).getContentForSection(\"html\", specialElementsUtil));\n";
        Assertions.assertEquals(expected, element.write().toString());
    }
}
