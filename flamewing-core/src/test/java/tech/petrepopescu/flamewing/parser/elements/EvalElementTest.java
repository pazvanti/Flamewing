package tech.petrepopescu.flamewing.parser.elements;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tech.petrepopescu.flamewing.parser.ElementFactory;

import java.util.List;

class EvalElementTest {
    @Test
    void evalElement() {
        String line = "@(a + b)";
        EvalElement element = new EvalElement(List.of(line), 0, null, ElementFactory.DEFAULT_BUILDER_NAME);
        element.parse("");
        Assertions.assertEquals("contentBuilder.append((a + b));", element.write().toString().trim());
    }
}
