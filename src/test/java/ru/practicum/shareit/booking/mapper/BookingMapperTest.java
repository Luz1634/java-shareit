package ru.practicum.shareit.booking.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class BookingMapperTest {

    @InjectMocks
    private BookingMapper mapper;

    @Test
    void toBooking() {
        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setStart(LocalDateTime.now());
        bookingRequest.setEnd(LocalDateTime.now());

        Booking booking = new Booking();
        booking.setStart(bookingRequest.getStart());
        booking.setEnd(bookingRequest.getEnd());

        assertEquals(booking, mapper.toBooking(bookingRequest));
    }

    @Test
    void toBookingResponse() {
        Booking booking = new Booking();
        booking.setId(1);
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now());
        booking.setItem(new Item());
        booking.setBooker(new User());
        booking.setStatus(BookingStatus.APPROVED);

        BookingResponse bookingResponse = new BookingResponse();
        bookingResponse.setId(1);
        bookingResponse.setStart(booking.getStart());
        bookingResponse.setEnd(booking.getEnd());
        bookingResponse.setItem(new Item());
        bookingResponse.setBooker(new User());
        bookingResponse.setStatus(BookingStatus.APPROVED);

        assertEquals(bookingResponse, mapper.toBookingResponse(booking));
    }
}