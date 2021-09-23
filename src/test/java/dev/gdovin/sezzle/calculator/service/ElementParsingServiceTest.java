package dev.gdovin.sezzle.calculator.service;

import dev.gdovin.sezzle.calculator.domain.Expression;
import dev.gdovin.sezzle.calculator.domain.Operand;
import dev.gdovin.sezzle.calculator.domain.Operator;
import dev.gdovin.sezzle.calculator.exception.UnparsableExpressionException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ElementParsingServiceTest {

    private final ElementParsingService service = new ElementParsingService();

    @ParameterizedTest
    @CsvSource({
            "2 + 1",
            "2 - 1",
            "2 - 1 + 1",
            "2 / 1",
            "2  + 1 * 0"
    })
    void shouldParseSupportedCharacters(String input) {
        assertDoesNotThrow(() -> service.parseIntoElements(input));
    }

    @Test
    void shouldParseIntoCorrectListOfElements() {
        String input = "-2 + 4 * 5     / 1 + 0  ";
        Expression expected = new Expression(Operand.of(-2), Operator.PLUS, Operand.of(4), Operator.MULTIPLY,
                Operand.of(5), Operator.DIVIDE, Operand.of(1), Operator.PLUS, Operand.of(0));

        Expression actual = service.parseIntoElements(input);

        assertThat(actual, is(expected));
    }

    @ParameterizedTest
    @CsvSource({
            "1 + b",
            "b + 1",
            "1 + dva",
            "1 mod 2",
            "(1 + 2) - 1",
            "1 minus 0",
            "--1 + 1",
    })
    void shouldFailToParseOtherCharacters(String input) {
        assertThrows(UnparsableExpressionException.class, () -> service.parseIntoElements(input));
    }

    @ParameterizedTest
    @MethodSource("paramsForEmptyInputs")
    void shouldFailToParseEmptyOrNullInput(String input) {
        assertThrows(UnparsableExpressionException.class, () -> service.parseIntoElements(input));
    }

    private static Stream<String> paramsForEmptyInputs() {
        return Stream.of(
              null,
              "",
              "   ",
              "     ",
              "\n"
        );
    }

    @Test
    void shouldThrowErrorContainingInvalidCharacters() {
        String input = "--2 + b * 1";

        UnparsableExpressionException thrown = assertThrows(UnparsableExpressionException.class,
                () -> service.parseIntoElements(input));

        assertThat(thrown.getMessage(), containsString("--2"));
        assertThat(thrown.getMessage(), containsString("b"));
    }
}