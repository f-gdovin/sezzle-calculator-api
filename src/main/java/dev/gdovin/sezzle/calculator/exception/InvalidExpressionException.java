package dev.gdovin.sezzle.calculator.exception;

import static java.lang.String.format;


public class InvalidExpressionException extends AbstractCalculatingException {

    private static final String ERROR_TEMPLATE = "Could not evaluate this expression. Reason - '%s'";

    public InvalidExpressionException(String errorReason) {
        super(format(ERROR_TEMPLATE, errorReason));
    }
}
