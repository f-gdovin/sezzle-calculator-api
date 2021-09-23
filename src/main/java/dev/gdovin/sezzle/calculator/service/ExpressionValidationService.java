package dev.gdovin.sezzle.calculator.service;

import dev.gdovin.sezzle.calculator.domain.InputElement;
import dev.gdovin.sezzle.calculator.domain.Operand;
import dev.gdovin.sezzle.calculator.exception.InvalidExpressionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.lang.String.format;

@Slf4j
@Service
public class ExpressionValidationService {

    public void validateSemantics(List<InputElement> inputElements) {
        validateOperatorsAreBetweenOperands(inputElements);
    }

    public Double validateResult(List<InputElement> outputElements) {
        if (outputElements.size() != 1 || !outputElements.get(0).isOperand()) {
            throwResultIsNotASingleNumberError(outputElements);
        }
        return ((Operand) outputElements.get(0)).getValue();
    }

    private void validateOperatorsAreBetweenOperands(List<InputElement> inputElements) {
        log.debug("About to validate the semantics of an expression: {}.", inputElements);
        verifyListStartsAndEndsWithOperands(inputElements);
        verifyThereAreNotExactlyTwoElements(inputElements);

        for (int index = 0; index < inputElements.size() - 1; index++) {
            InputElement left = inputElements.get(index);
            InputElement right = inputElements.get(index + 1);

            if (left.isOperand() && right.isOperand()) {
                throwNoOperatorBetweenOperandsError(left, right);
            }

            if (!left.isOperand() && !right.isOperand()) {
                throwNoOperandBetweenOperatorsError(left, right);
            }
        }
        log.debug("Expression {} is semantically correct.", inputElements);
    }

    private static void verifyListStartsAndEndsWithOperands(List<InputElement> inputElements) {
        if (!inputElements.get(0).isOperand() || !inputElements.get(inputElements.size() - 1).isOperand()) {
            log.info("Expression {} is invalid: it does not start and end with an operand, exiting now.", inputElements);
            throw new InvalidExpressionException("expected an operand at both the start and the end of an expression," +
                    " but got an operator");
        }
    }

    private static void verifyThereAreNotExactlyTwoElements(List<InputElement> inputElements) {
        if (inputElements.size() == 2) {
            log.info("Expression {} is invalid: it contains exactly two elements, exiting now.", inputElements);
            throwNoOperatorBetweenOperandsError(inputElements.get(0), inputElements.get(1));
        }
    }

    private static void throwNoOperatorBetweenOperandsError(InputElement left, InputElement right) {
        log.info("Expression is invalid: there is no operator to apply to '{}' and '{}', exiting now.", left, right);
        throw new InvalidExpressionException(format(
                "found two operands ('%s', '%s') without an operator between them", left, right));
    }

    private static void throwNoOperandBetweenOperatorsError(InputElement left, InputElement right) {
        log.info("Expression is invalid: there is no operand to be applied to '{}' and '{}', exiting now.", left, right);
        throw new InvalidExpressionException(format(
                "found two operators ('%s', '%s') without an operand between them", left, right));
    }

    private static void throwResultIsNotASingleNumberError(List<InputElement> output) {
        log.info("Result {} is invalid: it is not a single number.", output);
        throw new InvalidExpressionException(format("expected a single number as a result, but got '%s'", output));
    }
}
