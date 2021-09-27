package dev.gdovin.sezzle.calculator.service;

import dev.gdovin.sezzle.calculator.domain.Expression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CalculatorService {

    private final ElementParsingService elementParsingService;
    private final ExpressionValidationService expressionValidationService;
    private final ExpressionEvaluationService expressionEvaluationService;

    public double calculate(String input) {
        log.info("About to evaluate the expression '{}'", input);
        Expression expression = elementParsingService.parseIntoElements(input);
        expressionValidationService.validateSemantics(expression);

        Expression outputElements = expressionEvaluationService.evaluate(expression);
        final double result = expressionValidationService.validateResult(outputElements);

        log.info("Expression successfully evaluated to '{}'", result);
        return result;
    }
}
