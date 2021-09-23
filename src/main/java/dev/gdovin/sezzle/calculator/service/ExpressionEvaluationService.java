package dev.gdovin.sezzle.calculator.service;

import dev.gdovin.sezzle.calculator.domain.Expression;
import dev.gdovin.sezzle.calculator.domain.InputElement;
import dev.gdovin.sezzle.calculator.domain.Operand;
import dev.gdovin.sezzle.calculator.domain.Operator;
import dev.gdovin.sezzle.calculator.exception.InvalidExpressionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.lang.String.format;

@Slf4j
@Service
public class ExpressionEvaluationService {

    public Expression evaluate(Expression expression) {
        return doEvaluate(expression);
    }

    private Expression doEvaluate(Expression expression) {
        log.debug("About to evaluate the following expression: '{}'.", expression);

        int expressionStartIndex = findStartOfExpressionWithHighestWeight(expression);

        // exit condition, we have no more expressions to simplify
        if (expressionStartIndex < 0) {
            log.debug("There is no operator left to evaluate, returning the current expression '{}'.", expression);
            return expression;
        }

        // remove from this position three times, as rest shifts
        InputElement leftElem = expression.remove(expressionStartIndex);
        InputElement operator = expression.remove(expressionStartIndex);
        InputElement rightElm = expression.remove(expressionStartIndex);
        log.debug("About to evaluate the following sub-expression: '{} {} {}'", leftElem, operator, rightElm);

        InputElement simplified = simplifyExpression(leftElem, operator, rightElm);
        log.debug("Subexpression: '{} {} {}' evaluated to '{}'", leftElem, operator, rightElm, simplified);

        expression.add(expressionStartIndex, simplified);

        log.debug("Expression {} was successfully simplified, running next iteration.", expression);
        return doEvaluate(expression);
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

    private int findStartOfExpressionWithHighestWeight(Expression expression) {
        int highestOperatorWeight = -1;
        int highestOperatorIndex = -1;

        for (int index = 0; index < expression.size(); index ++) {
            InputElement el = expression.get(index);
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
