package ru.practicum.shareit.item.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class ItemBookingInfoForOwnerTest {

    @Autowired
    private JacksonTester<ItemBookingInfoForOwner> json;

    @Test
    @SneakyThrows
    void itemBookingInfoForOwner() {
        ItemBookingInfoForOwner item = new ItemBookingInfoForOwner(1, 2L, LocalDateTime.of(1, 1, 1, 1, 1, 1, 1), LocalDateTime.of(2, 1, 1, 1, 1, 1, 1));

        JsonContent<ItemBookingInfoForOwner> jsonItem = json.write(item);

        assertThat(jsonItem).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(jsonItem).extractingJsonPathNumberValue("$.bookerId").isEqualTo(2);
        assertThat(jsonItem).extractingJsonPathStringValue("$.start").isEqualTo("0001-01-01T01:01:01.000000001");
        assertThat(jsonItem).extractingJsonPathStringValue("$.end").isEqualTo("0002-01-01T01:01:01.000000001");
    }

}