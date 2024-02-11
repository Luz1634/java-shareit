package ru.practicum.shareit.booking.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class BookingResponseTest {

    @Autowired
    private JacksonTester<BookingResponse> json;

    @Test
    @SneakyThrows
    void bookingResponse() {
        BookingResponse booking = new BookingResponse(
                1,
                LocalDateTime.of(1, 1, 1, 1, 1, 1, 1),
                LocalDateTime.of(2, 1, 1, 1, 1, 1, 1),
                null,
                null,
                BookingStatus.APPROVED);

        JsonContent<BookingResponse> jsonBooking = json.write(booking);

        assertThat(jsonBooking).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(jsonBooking).extractingJsonPathStringValue("$.start").isEqualTo("0001-01-01T01:01:01.000000001");
        assertThat(jsonBooking).extractingJsonPathStringValue("$.end").isEqualTo("0002-01-01T01:01:01.000000001");
        assertThat(jsonBooking).extractingJsonPathValue("$.item").isEqualTo(null);
        assertThat(jsonBooking).extractingJsonPathValue("$.booker").isEqualTo(null);
        assertThat(jsonBooking).extractingJsonPathStringValue("$.status").isEqualTo("APPROVED");
    }
}