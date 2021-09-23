package dev.gdovin.sezzle.calculator.service;

import dev.gdovin.sezzle.calculator.domain.Expression;
import dev.gdovin.sezzle.calculator.domain.InputElement;
import dev.gdovin.sezzle.calculator.domain.Operand;
import dev.gdovin.sezzle.calculator.domain.Operator;
import dev.gdovin.sezzle.calculator.exception.UnparsableExpressionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ElementParsingService {

    public Expression parseIntoElements(String input) {
        if (input == null || input.trim().isEmpty()) {
            log.info("Expression '{}' is either null, or empty, exiting now.", input);
            throw new UnparsableExpressionException(input);
        }

        String[] elements = input.trim().split("\\s+");
        log.debug("Got following elements by splitting on whitespaces: {}.", Arrays.asList(elements));

        Expression parsed = new Expression();
        List<String> nonparsables = new ArrayList<>();

        for (String elementToParse : elements) {
            log.debug("About to parse element '{}'.", elementToParse);
            InputElement parsedElement = parseInputElement(elementToParse);

            if (parsedElement == null) {
                log.debug("Element '{}' cannot be parsed into any acceptable operand or operator.", elementToParse);
                nonparsables.add(elementToParse);
            } else {
                log.debug("Element parsed as '{}'.", parsedElement);
                parsed.add(parsedElement);
            }
        }

        if (!nonparsables.isEmpty()) {
            log.info("There were un-parsable elements '{}' in the expression {}, exiting now.", nonparsables, input);
            throw new UnparsableExpressionException(nonparsables);
        }

        return parsed;
    }

    private InputElement parseInputElement(String value) {
        Optional<Operand> operand = parseAsOperand(value);
        if (operand.isPresent()) {
            return operand.get();
        }

        Optional<Operator> operator = parseAsOperator(value);
        return operator.orElse(null);

    }

    private Optional<Operand> parseAsOperand(String value) {
        try {
            double numericValue = Double.parseDouble(value);
            return Optional.of(new Operand(numericValue));
        } catch (NumberFormatException formatException) {
            return Optional.empty();
        }
    }

    private Optional<Operator> parseAsOperator(String value) {
        return Arrays.stream(Operator.values())
                .filter(op -> op.getSymbol().equals(value))
                .findFirst();
    }
}
