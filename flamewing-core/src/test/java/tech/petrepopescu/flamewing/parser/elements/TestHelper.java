package tech.petrepopescu.flamewing.parser.elements;

import java.util.List;
import java.util.Set;
import tech.petrepopescu.flamewing.parser.ElementFactory;
import tech.petrepopescu.flamewing.parser.VariableRegistry;
import tech.petrepopescu.utils.TestUtil;

public class TestHelper {
    public static void main(String[] args) {
        String line = "<span class=\"fw-semibold\">@project.getLatitude().toString().substring(0,6), @project.getLongitude().toString().substring(0,6)</span>";
        HtmlElement element = new HtmlElement(List.of(line), 0, new ElementFactory(Set.of()), ElementFactory.DEFAULT_BUILDER_NAME);
        VariableRegistry.getInstance().add("", "project", "Project");
        element.parse("");
        System.out.println("--- ACTUAL ---");
        System.out.println(TestUtil.sanitizeResult(element.write().toString()));
        System.out.println("--------------");
    }
}
