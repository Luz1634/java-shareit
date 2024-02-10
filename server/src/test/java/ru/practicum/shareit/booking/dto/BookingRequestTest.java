package ru.practicum.shareit.booking.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class BookingRequestTest {

    @Autowired
    private JacksonTester<BookingRequest> json;

    @Test
    @SneakyThrows
    void bookingRequest() {
        BookingRequest booking = new BookingRequest(LocalDateTime.of(1, 1, 1, 1, 1, 1, 1), LocalDateTime.of(2, 1, 1, 1, 1, 1, 1), 1L);

        JsonContent<BookingRequest> jsonBooking = json.write(booking);

        assertThat(jsonBooking).extractingJsonPathStringValue("$.start").isEqualTo("0001-01-01T01:01:01.000000001");
        assertThat(jsonBooking).extractingJsonPathStringValue("$.end").isEqualTo("0002-01-01T01:01:01.000000001");
        assertThat(jsonBooking).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
    }
}