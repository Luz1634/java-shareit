package ru.practicum.shareit.request.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemInfoForItemRequest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class ItemRequestResponseTest {

    @Autowired
    private JacksonTester<ItemRequestResponse> json;

    @Test
    @SneakyThrows
    void itemRequestResponse() {
        ItemRequestResponse item = new ItemRequestResponse(
                1,
                "description",
                LocalDateTime.of(1, 1, 1, 1, 1, 1, 1),
                List.of(new ItemInfoForItemRequest(1, "name Item", "description Item", true, 2)));

        JsonContent<ItemRequestResponse> jsonItem = json.write(item);

        assertThat(jsonItem).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(jsonItem).extractingJsonPathStringValue("$.description").isEqualTo("description");
        assertThat(jsonItem).extractingJsonPathStringValue("$.created").isEqualTo("0001-01-01T01:01:01.000000001");
        assertThat(jsonItem).extractingJsonPathNumberValue("$.items[0].id").isEqualTo(1);
        assertThat(jsonItem).extractingJsonPathStringValue("$.items[0].name").isEqualTo("name Item");
        assertThat(jsonItem).extractingJsonPathStringValue("$.items[0].description").isEqualTo("description Item");
        assertThat(jsonItem).extractingJsonPathBooleanValue("$.items[0].available").isEqualTo(true);
        assertThat(jsonItem).extractingJsonPathNumberValue("$.items[0].requestId").isEqualTo(2);
    }
}