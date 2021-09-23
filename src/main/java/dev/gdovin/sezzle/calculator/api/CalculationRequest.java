package dev.gdovin.sezzle.calculator.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(onConstructor = @__(@JsonCreator))
public class CalculationRequest {

    private final String input;
}
