package tech.petrepopescu.flamewing.parser.elements.builders;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VariableElementBuilderTest {
    @Test
    void variableCorrectlyIdentified() {
        String line = "@a.b()";
        VariableElementBuilder variableElementBuilder = new VariableElementBuilder();
        assertTrue(variableElementBuilder.isValid(line));
    }

    @Test
    void variableMultiCallCorrectlyIdentified() {
        String line = "@a.b().c()";
        VariableElementBuilder variableElementBuilder = new VariableElementBuilder();
        assertTrue(variableElementBuilder.isValid(line));
    }

    @Test
    void variableCallsVariableCallsMethod() {
        String line = "@a.b.c()";
        VariableElementBuilder variableElementBuilder = new VariableElementBuilder();
        assertTrue(variableElementBuilder.isValid(line));
    }

    @Test
    void fragmentCallNotIdentifiedAsVariable() {
        String line = "@a.b.template()";
        VariableElementBuilder variableElementBuilder = new VariableElementBuilder();
        assertFalse(variableElementBuilder.isValid(line));
    }
}
