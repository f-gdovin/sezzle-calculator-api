package dev.gdovin.sezzle.calculator.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * See https://thirdspacelearning.com/blog/what-is-bodmas/#:~:text=Bodmas%20stands%20for%20Brackets%2C%20Orders,%2FMultiplication%2C%20Addition%2FSubtraction.
 * for BODMAS meaning, but TL,DR: BODMAS = Brackets, Orders, Division/Multiplication, Addition/Subtraction
 */

@RequiredArgsConstructor
@Getter
public enum OperatorWeight {
    B(6),
    O(5),
    D(4),
    M(3),
    A(2),
    S(1);

    private final int weight;
}
