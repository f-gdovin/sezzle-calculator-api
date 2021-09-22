package dev.gdovin.sezzle.calculator.domain;

import lombok.Value;

@Value
public class Operand implements InputElement {

    public static Operand of(double value) {
        return new Operand(value);
    }

    double value;
}
