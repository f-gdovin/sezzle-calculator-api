package dev.gdovin.sezzle.calculator.api;

import dev.gdovin.sezzle.calculator.domain.CalculationRequest;
import dev.gdovin.sezzle.calculator.domain.CalculationResult;
import dev.gdovin.sezzle.calculator.service.CalculatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CalculatorController {

    private final CalculatorService service;

    @PostMapping("/calculate")
    public CalculationResult calculate(CalculationRequest request) {
        return new CalculationResult(service.calculate(request.getInput()));
    }
}
