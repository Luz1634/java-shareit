package ru.practicum.shareit.item.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class ItemControllerRequestTest {

    @Autowired
    private JacksonTester<ItemControllerRequest> json;

    @Test
    @SneakyThrows
    void itemControllerRequest() {
        ItemControllerRequest item = new ItemControllerRequest("name", "description", true, 1L);

        JsonContent<ItemControllerRequest> jsonItem = json.write(item);

        assertThat(jsonItem).extractingJsonPathStringValue("$.name").isEqualTo("name");
        assertThat(jsonItem).extractingJsonPathStringValue("$.description").isEqualTo("description");
        assertThat(jsonItem).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(jsonItem).extractingJsonPathNumberValue("$.requestId").isEqualTo(1);
    }
}