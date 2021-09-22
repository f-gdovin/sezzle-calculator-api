package dev.gdovin.sezzle.calculator.exception;

public class DivisionByZeroException extends AbstractCalculatingException {

    public DivisionByZeroException() {
        super("Division by zero is not supported");
    }
}
