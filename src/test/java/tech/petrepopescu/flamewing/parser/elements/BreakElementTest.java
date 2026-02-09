package tech.petrepopescu.flamewing.parser.elements;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tech.petrepopescu.flamewing.parser.ElementFactory;

import java.util.List;
import java.util.Set;

class BreakElementTest {
    @Test
    void breakProperlyWritten() {
        final String line = "@break";
        BreakElement element = new BreakElement(List.of(line), 0, new ElementFactory(Set.of()), ElementFactory.DEFAULT_BUILDER_NAME);
        element.parse("");

        final String expected = "break;";
        Assertions.assertEquals(expected, element.write().toString().trim());
    }
}
