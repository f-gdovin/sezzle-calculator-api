package dev.gdovin.sezzle.calculator.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Expression extends ArrayList<InputElement> {

    public Expression(InputElement... elements) {
        this.addAll(Arrays.asList(elements));
    }

    @Override
    public String toString() {
        return this.stream()
                .map(InputElement::toString)
                .collect(Collectors.joining(" "));
    }
}
