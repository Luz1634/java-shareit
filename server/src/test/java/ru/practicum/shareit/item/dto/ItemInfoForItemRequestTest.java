package ru.practicum.shareit.item.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class ItemInfoForItemRequestTest {

    @Autowired
    private JacksonTester<ItemInfoForItemRequest> json;

    @Test
    @SneakyThrows
    void itemInfoForItemRequest() {
        ItemInfoForItemRequest item = new ItemInfoForItemRequest(1, "name", "description", true, 2);

        JsonContent<ItemInfoForItemRequest> jsonItem = json.write(item);

        assertThat(jsonItem).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(jsonItem).extractingJsonPathStringValue("$.name").isEqualTo("name");
        assertThat(jsonItem).extractingJsonPathStringValue("$.description").isEqualTo("description");
        assertThat(jsonItem).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(jsonItem).extractingJsonPathNumberValue("$.requestId").isEqualTo(2);
    }

}