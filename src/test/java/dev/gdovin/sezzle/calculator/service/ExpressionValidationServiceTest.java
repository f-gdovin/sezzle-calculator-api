package dev.gdovin.sezzle.calculator.service;

import dev.gdovin.sezzle.calculator.domain.Expression;
import dev.gdovin.sezzle.calculator.domain.Operand;
import dev.gdovin.sezzle.calculator.domain.Operator;
import dev.gdovin.sezzle.calculator.exception.InvalidExpressionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ExpressionValidationServiceTest {

    private final ExpressionValidationService service = new ExpressionValidationService();

    @ParameterizedTest
    @MethodSource("paramsForValidExpressions")
    @DisplayName("Should successfully validate the semantics")
    void shouldSuccessfullyValidateValidExpression(Expression input) {
        assertDoesNotThrow(() -> service.validateSemantics(input));
    }

    private static Stream<Expression> paramsForValidExpressions() {
        return Stream.of(
                new Expression(Operand.of(5)),
                new Expression(Operand.of(5), Operator.PLUS, Operand.of(2)),
                new Expression(Operand.of(5), Operator.DIVIDE, Operand.of(1)),
                new Expression(Operand.of(5), Operator.DIVIDE, Operand.of(1), Operator.PLUS, Operand.of(4))
        );
    }

    @ParameterizedTest
    @MethodSource("paramsForInvalidExpressions")
    @DisplayName("Should fail to validate the semantics")
    void shouldFailOnInvalidExpression(Expression input) {
        assertThrows(InvalidExpressionException.class, () -> service.validateSemantics(input));
    }

    private static Stream<Expression> paramsForInvalidExpressions() {
        return Stream.of(
                new Expression(Operator.PLUS),
                new Expression(Operand.of(5), Operand.of(2)),
                new Expression(Operand.of(5), Operand.of(4), Operand.of(2)),
                new Expression(Operand.of(5), Operator.PLUS),
                new Expression(Operator.MINUS, Operand.of(5)),
                new Expression(Operator.MINUS, Operator.DIVIDE),
                new Expression(Operand.of(5), Operator.DIVIDE, Operator.DIVIDE, Operand.of(1)),
                new Expression(Operand.of(5), Operator.DIVIDE, Operand.of(2), Operand.of(1))
        );
    }

    @ParameterizedTest
    @MethodSource("paramsForValidResult")
    @DisplayName("Should successfully validate and return the result")
    void shouldSuccessfullyValidateResult(Expression expression, double expected) {
        double actual = service.validateResult(expression);

        assertThat(actual, is(expected));
    }

    private static Stream<Arguments> paramsForValidResult() {
        return Stream.of(
                Arguments.of(new Expression(Operand.of(42)),       42),
                Arguments.of(new Expression(Operand.of(-5)),       -5),
                Arguments.of(new Expression(Operand.of(0.05)),   0.05)
        );
    }

    @ParameterizedTest
    @MethodSource("paramsForInvalidResult")
    @DisplayName("Should fail to validate the result")
    void shouldFailOnInvalidResult(Expression input) {
        assertThrows(InvalidExpressionException.class, () -> service.validateResult(input));
    }

    private static Stream<Expression> paramsForInvalidResult() {
        return Stream.of(
                new Expression(Operator.PLUS),
                new Expression(Operand.of(5), Operand.of(2)),
                new Expression(Operand.of(5), Operator.PLUS),
                new Expression(Operator.MINUS, Operand.of(5)),
                new Expression(Operator.MINUS, Operator.DIVIDE),
                new Expression(Operand.of(5), Operator.DIVIDE, Operator.DIVIDE, Operand.of(1)),
                new Expression(Operand.of(5), Operator.DIVIDE, Operand.of(2), Operand.of(1))
        );
    }

}