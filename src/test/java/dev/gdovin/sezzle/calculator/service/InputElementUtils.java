package dev.gdovin.sezzle.calculator.service;

import dev.gdovin.sezzle.calculator.domain.InputElement;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class InputElementUtils {

    public static List<InputElement> elements(InputElement... elements) {
        return Arrays.stream(elements).collect(Collectors.toList());
    }
}
