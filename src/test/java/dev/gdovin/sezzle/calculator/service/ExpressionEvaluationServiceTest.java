package dev.gdovin.sezzle.calculator.service;

import dev.gdovin.sezzle.calculator.domain.Expression;
import dev.gdovin.sezzle.calculator.domain.InputElement;
import dev.gdovin.sezzle.calculator.domain.Operand;
import dev.gdovin.sezzle.calculator.domain.Operator;
import dev.gdovin.sezzle.calculator.exception.DivisionByZeroException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ExpressionEvaluationServiceTest {

    private final ExpressionEvaluationService service = new ExpressionEvaluationService();

    @Test
    @DisplayName("Should correctly evaluate and simplify the expression")
    void shouldCorrectlyEvaluateListOfElements() {
        // "-2 + 4 * 15 / 0.25 + 0.01"
        Expression input = new Expression(
                Operand.of(-2), Operator.PLUS, Operand.of(4), Operator.MULTIPLY, Operand.of(15),
                Operator.DIVIDE, Operand.of(0.25), Operator.PLUS, Operand.of(0.01)
        );

        Expression actual = service.evaluate(input);

        assertThat(actual.size(), is(1));
        assertThat(actual.get(0), is(Operand.of(238.01)));
    }

    @ParameterizedTest
    @MethodSource("paramsForNearZeroDivision")
    @DisplayName("Should correctly divide by almost a zero")
    void shouldCorrectlyDivideByAlmostZero(InputElement almostZero) {
        Expression input = new Expression(Operand.of(10), Operator.DIVIDE, almostZero);

        assertDoesNotThrow(() -> service.evaluate(input));
    }

    private static Stream<InputElement> paramsForNearZeroDivision() {
        return Stream.of(
                Operand.of(0.01),
                Operand.of(-0.01),
                Operand.of(0.0000001),
                Operand.of(-0.0000001)
        );
    }

    @Test
    @DisplayName("Should fail to evaluate division by zero")
    void shouldFailWithDivisionByZero() {
        Expression input = new Expression(Operand.of(5), Operator.DIVIDE, Operand.of(0));
        assertThrows(DivisionByZeroException.class, () -> service.evaluate(input));
    }
}