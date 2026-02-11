package tech.petrepopescu.flamewing.parser.elements;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tech.petrepopescu.flamewing.parser.ElementFactory;

import java.util.List;
import java.util.Set;

class EscapeCharacterElementTest {
    @Test
    void characterIsEscaped() {
        final String line = "@@domain.com";
        EscapeCharacterElement element = new EscapeCharacterElement(List.of(line), 0, new ElementFactory(Set.of()), ElementFactory.DEFAULT_BUILDER_NAME);

        element.parse("");

        final String expected = "\t\tcontentBuilder.append(\"@domain.com\");\n";
        Assertions.assertEquals(expected, element.write().toString());
    }
}
