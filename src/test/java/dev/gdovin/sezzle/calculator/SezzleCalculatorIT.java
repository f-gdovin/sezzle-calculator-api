package dev.gdovin.sezzle.calculator;

import dev.gdovin.sezzle.calculator.exception.InvalidExpressionException;
import dev.gdovin.sezzle.calculator.exception.UnparsableExpressionException;
import dev.gdovin.sezzle.calculator.service.CalculatorService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class SezzleCalculatorIT {

    @Autowired
    private CalculatorService service;

    @ParameterizedTest
    @MethodSource("paramsForSuccessfulCalculation")
    @DisplayName("Should calculate the result correctly given valid input")
    void shouldCalculateTheResultCorrectly(String input, double expected) {
        double actual = service.calculate(input);

        assertThat(actual, is(expected));
    }

    private static Stream<Arguments> paramsForSuccessfulCalculation() {
        return Stream.of(
                Arguments.of("5 + 2 * 3",             11),
                Arguments.of("5 + 3 * 2",             11),
                Arguments.of("-5 + 10 + 2 * 3",       11),
                Arguments.of("11 * 2 / 2",            11),
                Arguments.of("21 / 2 + 0.5",          11),
                Arguments.of("21.75 + -10.25 - 0.5",  11)
        );
    }

    @ParameterizedTest
    @MethodSource("paramsForSuccessfulCalculation_whitespaces")
    @DisplayName("Should calculate the result correctly and ignore whitespaces")
    void shouldCalculateTheResultCorrectlyAndIgnoreWhitespaces(String input) {
        double actual = service.calculate(input);

        assertThat(actual, is(11d));
    }

    private static Stream<String> paramsForSuccessfulCalculation_whitespaces() {
        return Stream.of(
                "  5 + 2 * 3",
                "5 + 2 * 3  ",
                "  5 + 2 * 3  ",
                "  5  +  2  * 3"
        );
    }

    @Test
    @DisplayName("Should fail to calculate the result on invalid input (missing whitespaces)")
    void shouldFailToCalculateTheResultOnMissingWhitespaces() {
        assertThrows(UnparsableExpressionException.class, () -> service.calculate("5+2*3"));
    }

    @ParameterizedTest
    @MethodSource("paramsForFailedCalculation_syntax")
    @DisplayName("Should fail to calculate the result on invalid input (invalid syntax)")
    void shouldFailToCalculateTheResultOnInvalidSyntax(String input) {
        assertThrows(UnparsableExpressionException.class, () -> service.calculate(input));
    }

    private static Stream<String> paramsForFailedCalculation_syntax() {
        return Stream.of(
                "5 + 2 - b",
                "5 + 3 % 2",
                "-5 + 10 + 2 * ten"
        );
    }

    @ParameterizedTest
    @MethodSource("paramsForFailedCalculation_semantics")
    @DisplayName("Should fail to calculate the result on invalid input (invalid semantics)")
    void shouldFailToCalculateTheResultOnInvalidSemantics(String input) {
        assertThrows(InvalidExpressionException.class, () -> service.calculate(input));
    }

    private static Stream<String> paramsForFailedCalculation_semantics() {
        return Stream.of(
                "5 + 2 *",
                "+ 5 + 3 * 2",
                "- 5 + 10 + 2 * 3 -",
                "11 2",
                "+"
        );
    }

}
