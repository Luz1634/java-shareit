package ru.practicum.shareit.request.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class ItemRequestRequestTest {

    @Autowired
    private JacksonTester<ItemRequestRequest> json;

    @Test
    @SneakyThrows
    void itemRequestRequest() {
        ItemRequestRequest item = new ItemRequestRequest("description");

        JsonContent<ItemRequestRequest> jsonItem = json.write(item);

        assertThat(jsonItem).extractingJsonPathStringValue("$.description").isEqualTo("description");
    }

}