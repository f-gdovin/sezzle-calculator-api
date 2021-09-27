package dev.gdovin.sezzle.calculator.api;

import dev.gdovin.sezzle.calculator.service.CalculatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CrossOrigin("http://localhost:3000")
public class CalculatorController {

    private final CalculatorService service;

    @PostMapping("/calculate")
    public CalculationResult calculate(@RequestBody CalculationRequest request) {
        return new CalculationResult(service.calculate(request.getInput()));
    }
}
