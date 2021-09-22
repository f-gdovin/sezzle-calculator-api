package dev.gdovin.sezzle.calculator.exception;

import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.List;

import static java.lang.String.format;

@Value
@EqualsAndHashCode(callSuper = false)
public class UnparsableExpressionException extends AbstractCalculatingException {

    private static final String ERROR_TEMPLATE = "Got invalid characters, cannot evaluate: '%s'";

    public UnparsableExpressionException(List<String> invalidElements) {
        super(format(ERROR_TEMPLATE, invalidElements));
    }
}
