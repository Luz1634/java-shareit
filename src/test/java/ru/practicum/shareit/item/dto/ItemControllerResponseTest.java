package ru.practicum.shareit.item.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class ItemControllerResponseTest {

    @Autowired
    private JacksonTester<ItemControllerResponse> json;

    @Test
    @SneakyThrows
    void itemControllerResponse() {
        ItemControllerResponse item = new ItemControllerResponse(1, "name", "description", true, null, null, null, 2);

        JsonContent<ItemControllerResponse> jsonItem = json.write(item);

        assertThat(jsonItem).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(jsonItem).extractingJsonPathStringValue("$.name").isEqualTo("name");
        assertThat(jsonItem).extractingJsonPathStringValue("$.description").isEqualTo("description");
        assertThat(jsonItem).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(jsonItem).extractingJsonPathValue("$.lastBooking").isEqualTo(null);
        assertThat(jsonItem).extractingJsonPathValue("$.nextBooking").isEqualTo(null);
        assertThat(jsonItem).extractingJsonPathValue("$.comments").isEqualTo(null);
        assertThat(jsonItem).extractingJsonPathNumberValue("$.requestId").isEqualTo(2);
    }
}