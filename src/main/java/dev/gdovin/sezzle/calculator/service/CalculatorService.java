package dev.gdovin.sezzle.calculator.service;

import dev.gdovin.sezzle.calculator.domain.InputElement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CalculatorService {

    private final ElementParsingService elementParsingService;
    private final ExpressionValidationService expressionValidationService;
    private final ExpressionEvaluationService expressionEvaluationService;

    public double calculate(String input) {
        log.info("About to evaluate the expression '{}'", input);
        List<InputElement> inputElements = elementParsingService.parseIntoElements(input);
        expressionValidationService.validateSemantics(inputElements);

        List<InputElement> outputElements = expressionEvaluationService.evaluate(inputElements);
        final double result = expressionValidationService.validateResult(outputElements);

        log.info("Expression successfully evaluated to '{}'", result);
        return result;
    }
}
