package dev.gdovin.sezzle.calculator.service;

import dev.gdovin.sezzle.calculator.domain.Expression;
import dev.gdovin.sezzle.calculator.domain.InputElement;
import dev.gdovin.sezzle.calculator.domain.Operand;
import dev.gdovin.sezzle.calculator.exception.InvalidExpressionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static java.lang.String.format;

@Slf4j
@Service
public class ExpressionValidationService {

    public void validateSemantics(Expression expression) {
        validateOperatorsAreBetweenOperands(expression);
    }

    public Double validateResult(Expression output) {
        if (output.size() != 1 || !output.get(0).isOperand()) {
            throwResultIsNotASingleNumberError(output);
        }
        return ((Operand) output.get(0)).getValue();
    }

    private void validateOperatorsAreBetweenOperands(Expression expression) {
        log.debug("About to validate the semantics of an expression: {}.", expression);
        verifyListStartsAndEndsWithOperands(expression);
        verifyThereAreNotExactlyTwoElements(expression);

        for (int index = 0; index < expression.size() - 1; index++) {
            InputElement left = expression.get(index);
            InputElement right = expression.get(index + 1);

            if (left.isOperand() && right.isOperand()) {
                throwNoOperatorBetweenOperandsError(left, right);
            }

            if (!left.isOperand() && !right.isOperand()) {
                throwNoOperandBetweenOperatorsError(left, right);
            }
        }
        log.debug("Expression {} is semantically correct.", expression);
    }

    private static void verifyListStartsAndEndsWithOperands(Expression expression) {
        if (!expression.get(0).isOperand() || !expression.get(expression.size() - 1).isOperand()) {
            log.info("Expression {} is invalid: it does not start and end with an operand, exiting now.", expression);
            throw new InvalidExpressionException("expected an operand at both the start and the end of an expression," +
                    " but got an operator");
        }
    }

    private static void verifyThereAreNotExactlyTwoElements(Expression expression) {
        if (expression.size() == 2) {
            log.info("Expression {} is invalid: it contains exactly two elements, exiting now.", expression);
            throwNoOperatorBetweenOperandsError(expression.get(0), expression.get(1));
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

    private static void throwResultIsNotASingleNumberError(Expression output) {
        log.info("Result {} is invalid: it is not a single number.", output);
        throw new InvalidExpressionException(format("expected a single number as a result, but got '%s'", output));
    }
}
