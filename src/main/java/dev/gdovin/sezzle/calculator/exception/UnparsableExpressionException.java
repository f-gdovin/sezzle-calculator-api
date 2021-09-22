package dev.gdovin.sezzle.calculator.exception;

import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.List;

import static java.lang.String.format;

@Value
@EqualsAndHashCode(callSuper = false)
public class UnparsableExpressionException extends AbstractCalculatingException {

    private static final String ERROR_TEMPLATE = "Could not evaluate expression. Reason - '%s'";

    public UnparsableExpressionException(String input) {
        super(format(ERROR_TEMPLATE, "got invalid input: " + input));
    }

    public UnparsableExpressionException(List<String> invalidElements) {
        super(format(ERROR_TEMPLATE, "got invalid characters: " + invalidElements));
    }
}
