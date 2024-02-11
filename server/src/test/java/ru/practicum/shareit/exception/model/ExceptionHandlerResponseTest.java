package ru.practicum.shareit.exception.model;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class ExceptionHandlerResponseTest {

    @Autowired
    private JacksonTester<ExceptionHandlerResponse> json;

    @Test
    @SneakyThrows
    void exceptionHandlerResponse() {
        ExceptionHandlerResponse response = new ExceptionHandlerResponse("message", "error");

        JsonContent<ExceptionHandlerResponse> jsonException = json.write(response);

        assertThat(jsonException).extractingJsonPathStringValue("$.message").isEqualTo("message");
        assertThat(jsonException).extractingJsonPathStringValue("$.error").isEqualTo("error");
    }
}