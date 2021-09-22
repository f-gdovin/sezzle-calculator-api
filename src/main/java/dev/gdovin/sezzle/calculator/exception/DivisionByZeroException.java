package dev.gdovin.sezzle.calculator.exception;

public class DivisionByZeroException extends InvalidExpressionException {

    public DivisionByZeroException() {
        super("division by zero is not supported");
    }
}
