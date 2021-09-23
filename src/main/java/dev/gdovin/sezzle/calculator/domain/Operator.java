package dev.gdovin.sezzle.calculator.domain;

import dev.gdovin.sezzle.calculator.exception.DivisionByZeroException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Operator implements InputElement {
    PLUS("+") {
        @Override
        public Operand apply(Operand first, Operand second) {
            return Operand.of(first.getValue() + second.getValue());
        }

        @Override
        public int getWeight() {
            return OperatorWeight.A.getWeight();
        }
    },

    MINUS("-") {
        @Override
        public Operand apply(Operand first, Operand second) {
            return Operand.of(first.getValue() - second.getValue());
        }

        @Override
        public int getWeight() {
            return OperatorWeight.A.getWeight();
        }
    },

    MULTIPLY("*") {
        @Override
        public Operand apply(Operand first, Operand second) {
            return Operand.of(first.getValue() * second.getValue());
        }

        @Override
        public int getWeight() {
            return OperatorWeight.M.getWeight();
        }
    },

    DIVIDE("/") {
        @Override
        public Operand apply(Operand first, Operand second) {
            if (second.getValue() == 0) {
                throw new DivisionByZeroException();
            }
            return Operand.of(first.getValue() / second.getValue());
        }

        @Override
        public int getWeight() {
            return OperatorWeight.M.getWeight();
        }
    };

    private final String symbol;

    public abstract Operand apply(Operand first, Operand second);
    public abstract int getWeight();

    @Override
    public String toString() {
        return symbol;
    }
}
