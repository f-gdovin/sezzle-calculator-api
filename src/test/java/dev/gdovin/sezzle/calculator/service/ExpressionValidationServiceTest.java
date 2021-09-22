package dev.gdovin.sezzle.calculator.service;

import dev.gdovin.sezzle.calculator.domain.InputElement;
import dev.gdovin.sezzle.calculator.domain.Operand;
import dev.gdovin.sezzle.calculator.domain.Operator;
import dev.gdovin.sezzle.calculator.exception.InvalidExpressionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static dev.gdovin.sezzle.calculator.service.InputElementUtils.elements;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ExpressionValidationServiceTest {

    private final ExpressionValidationService service = new ExpressionValidationService();

    @ParameterizedTest
    @MethodSource("paramsForValidExpressions")
    @DisplayName("Should successfully validate the semantics")
    void shouldSuccessfullyValidateValidExpression(List<InputElement> input) {
        assertDoesNotThrow(() -> service.validateSemantics(input));
    }

    private static Stream<List<InputElement>> paramsForValidExpressions() {
        return Stream.of(
                elements(Operand.of(5)),
                elements(Operand.of(5), Operator.PLUS, Operand.of(2)),
                elements(Operand.of(5), Operator.DIVIDE, Operand.of(1)),
                elements(Operand.of(5), Operator.DIVIDE, Operand.of(1), Operator.PLUS, Operand.of(4))
        );
    }

    @ParameterizedTest
    @MethodSource("paramsForInvalidExpressions")
    @DisplayName("Should fail to validate the semantics")
    void shouldFailOnInvalidExpression(List<InputElement> input) {
        assertThrows(InvalidExpressionException.class, () -> service.validateSemantics(input));
    }

    private static Stream<List<InputElement>> paramsForInvalidExpressions() {
        return Stream.of(
                elements(Operator.PLUS),
                elements(Operand.of(5), Operand.of(2)),
                elements(Operand.of(5), Operand.of(4), Operand.of(2)),
                elements(Operand.of(5), Operator.PLUS),
                elements(Operator.MINUS, Operand.of(5)),
                elements(Operator.MINUS, Operator.DIVIDE),
                elements(Operand.of(5), Operator.DIVIDE, Operator.DIVIDE, Operand.of(1)),
                elements(Operand.of(5), Operator.DIVIDE, Operand.of(2), Operand.of(1))
        );
    }

    @ParameterizedTest
    @MethodSource("paramsForValidResult")
    @DisplayName("Should successfully validate and return the result")
    void shouldSuccessfullyValidateResult(List<InputElement> elements, double expected) {
        double actual = service.validateResult(elements);

        assertThat(actual, is(expected));
    }

    private static Stream<Arguments> paramsForValidResult() {
        return Stream.of(
                Arguments.of(elements(Operand.of(42)),       42),
                Arguments.of(elements(Operand.of(-5)),       -5),
                Arguments.of(elements(Operand.of(0.05)),   0.05)
        );
    }

    @ParameterizedTest
    @MethodSource("paramsForInvalidResult")
    @DisplayName("Should fail to validate the result")
    void shouldFailOnInvalidResult(List<InputElement> input) {
        assertThrows(InvalidExpressionException.class, () -> service.validateResult(input));
    }

    private static Stream<List<InputElement>> paramsForInvalidResult() {
        return Stream.of(
                elements(Operator.PLUS),
                elements(Operand.of(5), Operand.of(2)),
                elements(Operand.of(5), Operator.PLUS),
                elements(Operator.MINUS, Operand.of(5)),
                elements(Operator.MINUS, Operator.DIVIDE),
                elements(Operand.of(5), Operator.DIVIDE, Operator.DIVIDE, Operand.of(1)),
                elements(Operand.of(5), Operator.DIVIDE, Operand.of(2), Operand.of(1))
        );
    }

}