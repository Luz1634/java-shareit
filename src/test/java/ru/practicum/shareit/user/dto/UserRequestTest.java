package ru.practicum.shareit.user.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class UserRequestTest {

    @Autowired
    private JacksonTester<UserRequest> json;

    @Test
    @SneakyThrows
    void userRequest() {
        UserRequest userRequest = new UserRequest("name", "user@email.com");

        JsonContent<UserRequest> jsonUser = json.write(userRequest);

        assertThat(jsonUser).extractingJsonPathStringValue("$.name").isEqualTo("name");
        assertThat(jsonUser).extractingJsonPathStringValue("$.email").isEqualTo("user@email.com");
    }
}