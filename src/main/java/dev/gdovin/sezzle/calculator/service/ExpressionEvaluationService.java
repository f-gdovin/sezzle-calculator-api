package dev.gdovin.sezzle.calculator.service;

import dev.gdovin.sezzle.calculator.domain.InputElement;
import dev.gdovin.sezzle.calculator.domain.Operand;
import dev.gdovin.sezzle.calculator.domain.Operator;
import dev.gdovin.sezzle.calculator.exception.InvalidExpressionException;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.lang.String.format;

@Service
public class ExpressionEvaluationService {

    public List<InputElement> evaluate(List<InputElement> inputElements) {
        return doEvaluate(inputElements);
    }

    private List<InputElement> doEvaluate(List<InputElement> inputElements) {
        int expressionStartIndex = findStartOfExpressionWithHighestWeight(inputElements);

        // exit condition, we have no more expressions to simplify
        if (expressionStartIndex < 0) {
            return inputElements;
        }

        // remove from this position three times, as rest shifts
        InputElement leftElem = inputElements.remove(expressionStartIndex);
        InputElement operator = inputElements.remove(expressionStartIndex);
        InputElement rightElm = inputElements.remove(expressionStartIndex);

        InputElement simplified = simplifyExpression(leftElem, operator, rightElm);

        inputElements.add(expressionStartIndex, simplified);

        return doEvaluate(inputElements);
    }

    private InputElement simplifyExpression(InputElement left, InputElement center, InputElement right) {
        boolean validExpression = left.isOperand() && !center.isOperand() && right.isOperand();
        if (!validExpression) {
            throw new InvalidExpressionException(format("could not simplify expression '%s %s %s'," +
                    " as it is not a valid arithmetic expression", left, center, right));
        }

        Operand leftOperand = (Operand) left;
        Operator operator = (Operator) center;
        Operand rightOperand = (Operand) right;
        return operator.apply(leftOperand, rightOperand);
    }

    private int findStartOfExpressionWithHighestWeight(List<InputElement> inputElements) {
        int highestOperatorWeight = -1;
        int highestOperatorIndex = -1;

        for (int index = 0; index < inputElements.size(); index ++) {
            InputElement el = inputElements.get(index);
            if (!el.isOperand()) {
                Operator operator = (Operator) el;
                if (highestOperatorWeight < operator.getWeight()) {
                    highestOperatorWeight = operator.getWeight();
                    highestOperatorIndex = index;
                }
            }
        }

        // -1 to get the left operand of this operator
        return highestOperatorIndex - 1;
    }
}
