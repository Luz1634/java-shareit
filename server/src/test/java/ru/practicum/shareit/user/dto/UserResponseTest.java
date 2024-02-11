package ru.practicum.shareit.user.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class UserResponseTest {

    @Autowired
    private JacksonTester<UserResponse> json;

    @Test
    @SneakyThrows
    void userResponse() {
        UserResponse userResponse = new UserResponse(1, "name", "user@email.com");

        JsonContent<UserResponse> jsonUser = json.write(userResponse);

        assertThat(jsonUser).extractingJsonPathValue("$.id").isEqualTo(1);
        assertThat(jsonUser).extractingJsonPathStringValue("$.name").isEqualTo("name");
        assertThat(jsonUser).extractingJsonPathStringValue("$.email").isEqualTo("user@email.com");
    }
}