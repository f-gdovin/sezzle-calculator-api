package dev.gdovin.sezzle.calculator.service;

import dev.gdovin.sezzle.calculator.domain.InputElement;
import dev.gdovin.sezzle.calculator.domain.Operand;
import dev.gdovin.sezzle.calculator.exception.InvalidExpressionException;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.lang.String.format;

@Service
public class ExpressionValidationService {

    private static final Class<Operand> OPERAND_CLASS = Operand.class;

    public void validateSyntax(List<InputElement> inputElements) {
        validateOperatorsAreBetweenOperands(inputElements);
    }

    public Double validateResult(List<InputElement> outputElements) {
        if (outputElements.size() != 1 || !isOperand(outputElements.get(0))) {
            throwResultIsNotASingleNumberError(outputElements);
        }
        return ((Operand) outputElements.get(0)).getValue();
    }

    private void validateOperatorsAreBetweenOperands(List<InputElement> inputElements) {
        verifyListStartsAndEndsWithOperands(inputElements);
        verifyThereAreNotExactlyTwoElements(inputElements);

        for (int index = 0; index < inputElements.size() - 1; index++) {
            InputElement left = inputElements.get(index);
            InputElement right = inputElements.get(index + 1);

            if (isOperand(left) && isOperand(right)) {
                throwNoOperatorBetweenOperandsError(left, right);
            }

            if (!isOperand(left) && !isOperand(right)) {
                throwNoOperandBetweenOperatorsError(left, right);
            }
        }
    }

    private static void verifyListStartsAndEndsWithOperands(List<InputElement> inputElements) {
        if (!isOperand(inputElements.get(0)) || !isOperand(inputElements.get(inputElements.size() - 1))) {
            throw new InvalidExpressionException("expected an operand at both the start and the end of an expression," +
                    " but got an operator");
        }
    }

    private static void verifyThereAreNotExactlyTwoElements(List<InputElement> inputElements) {
        if (inputElements.size() == 2) {
            throwNoOperatorBetweenOperandsError(inputElements.get(0), inputElements.get(1));
        }
    }

    private static void throwNoOperatorBetweenOperandsError(InputElement left, InputElement right) {
        throw new InvalidExpressionException(format(
                "found two operands ('%s', '%s') without an operator between them", left, right));
    }

    private static void throwNoOperandBetweenOperatorsError(InputElement left, InputElement right) {
        throw new InvalidExpressionException(format(
                "found two operators ('%s', '%s') without an operand between them", left, right));
    }

    private static void throwResultIsNotASingleNumberError(List<InputElement> output) {
        throw new InvalidExpressionException(format("expected a single number as a result, but got '%s'", output));
    }

    private static boolean isOperand(InputElement element) {
        return OPERAND_CLASS.isInstance(element);
    }
}
