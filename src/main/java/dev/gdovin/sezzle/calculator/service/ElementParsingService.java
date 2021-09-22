package dev.gdovin.sezzle.calculator.service;

import dev.gdovin.sezzle.calculator.domain.*;
import dev.gdovin.sezzle.calculator.exception.InvalidExpressionException;
import dev.gdovin.sezzle.calculator.exception.UnparsableExpressionException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class ElementParsingService {

    public List<InputElement> parseIntoElements(String input) {
        String[] elements = input.trim().split("\\s+");

        List<InputElement> parsed = new ArrayList<>();
        List<String> nonparsables = new ArrayList<>();

        for (String elementToParse : elements) {
            InputElement parsedElement = parseInputElement(elementToParse);

            if (parsedElement == null) {
                nonparsables.add(elementToParse);
            } else {
                parsed.add(parsedElement);
            }
        }

        if (!nonparsables.isEmpty()) {
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
