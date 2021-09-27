package dev.gdovin.sezzle.calculator.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.gdovin.sezzle.calculator.exception.DivisionByZeroException;
import dev.gdovin.sezzle.calculator.exception.InvalidExpressionException;
import dev.gdovin.sezzle.calculator.exception.UnparsableExpressionException;
import dev.gdovin.sezzle.calculator.service.CalculatorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.ConcurrentModificationException;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CalculatorController.class)
class CalculatorControllerTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final String INPUT_STRING = "-5 + 10 * 2";
    private static final CalculationRequest INPUT = new CalculationRequest(INPUT_STRING);
    private static final CalculationResult OUTPUT = new CalculationResult(15.0);

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CalculatorService service;

    @Test
    public void shouldReturnResultIfCalculatedCorrectly() throws Exception {
        when(service.calculate(INPUT_STRING)).thenReturn(15d);

        postCalculationRequest()
                .andExpect(status().isOk())
                .andExpect(content().json(OBJECT_MAPPER.writeValueAsString(OUTPUT)));

        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(service).calculate(stringArgumentCaptor.capture());

        assertThat(stringArgumentCaptor.getValue(), is(INPUT_STRING));
    }

    @ParameterizedTest
    @MethodSource("paramsForErrors")
    public void shouldReturnCorrectStatusCodeBasedOnError(Exception thrown, ResultMatcher expectedStatusCode)
            throws Exception {
        when(service.calculate(INPUT_STRING)).thenThrow(thrown);

        postCalculationRequest()
                .andExpect(expectedStatusCode)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("error", is(notNullValue())));
    }

    private static Stream<Arguments> paramsForErrors() {
        return Stream.of(
                Arguments.of(new DivisionByZeroException(),                status().isBadRequest()),
                Arguments.of(new InvalidExpressionException("just fail"),  status().isBadRequest()),
                Arguments.of(new UnparsableExpressionException("failed"),  status().isBadRequest()),

                Arguments.of(new NullPointerException(),                   status().isInternalServerError()),
                Arguments.of(new IndexOutOfBoundsException(),              status().isInternalServerError()),
                Arguments.of(new ConcurrentModificationException(),        status().isInternalServerError())
        );
    }

    private ResultActions postCalculationRequest() throws Exception {
        return mockMvc.perform(post("/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.writeValueAsString(INPUT)));
    }

}