package tech.petrepopescu.flamewing.parser.elements;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tech.petrepopescu.flamewing.parser.ElementFactory;

import java.util.List;
import java.util.Set;

class AutowiredElementTest {
    @Test
    void simpleTest() {
        String line = "@autowired(MyRepository repo)";
        AutowiredElement element = new AutowiredElement(List.of(line), 0, new ElementFactory(Set.of()), ElementFactory.DEFAULT_BUILDER_NAME);
        element.parse("");

        String expected = "\t\tMyRepository repo = specialElementsUtil.getAutowiredObject(MyRepository.class);\n";
        Assertions.assertEquals(expected, element.write().toString());
    }
}
