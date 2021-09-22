package dev.gdovin.sezzle.calculator.domain;

public interface InputElement {

    Class<Operand> OPERAND_CLASS = Operand.class;

    default boolean isOperand() {
        return OPERAND_CLASS.isInstance(this);
    }
}
