package dev.gdovin.sezzle.calculator.api;

import dev.gdovin.sezzle.calculator.exception.AbstractCalculatingException;
import dev.gdovin.sezzle.calculator.service.CalculatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CalculatorController {

    private final CalculatorService service;

    @PostMapping("/calculate")
    public CalculationResult calculate(@RequestBody CalculationRequest request) {
        return new CalculationResult(service.calculate(request.getInput()));
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AbstractCalculatingException.class)
    public void handleExpectedErrors() {
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Unexpected server error, please see logs")
    @ExceptionHandler(Exception.class)
    public void handleUnexpectedErrors(Exception thrown) {
        log.warn("Uncaught exception " + thrown);
    }
}
