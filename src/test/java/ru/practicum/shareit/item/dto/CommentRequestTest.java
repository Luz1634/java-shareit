package ru.practicum.shareit.item.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class CommentRequestTest {

    @Autowired
    private JacksonTester<CommentRequest> json;

    @Test
    @SneakyThrows
    void commentRequest() {
        CommentRequest comment = new CommentRequest("text");

        JsonContent<CommentRequest> jsonComment = json.write(comment);

        assertThat(jsonComment).extractingJsonPathStringValue("$.text").isEqualTo("text");
    }
}