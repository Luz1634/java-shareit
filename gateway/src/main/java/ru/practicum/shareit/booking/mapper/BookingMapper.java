package ru.practicum.shareit.booking.mapper;

import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.user.model.User;

@Component
@RequiredArgsConstructor
public class BookingMapper {

    public Booking toBooking(BookingRequest bookingRequest) {
        Booking booking = new Booking();
        booking.setStart(bookingRequest.getStart());
        booking.setEnd(bookingRequest.getEnd());
        return booking;
    }

    public BookingResponse toBookingResponse(Booking booking) {
        BookingResponse bookingResponse = new BookingResponse();
        bookingResponse.setId(booking.getId());
        bookingResponse.setStart(booking.getStart());
        bookingResponse.setEnd(booking.getEnd());
        booking.getItem().setOwner((User) Hibernate.unproxy(booking.getItem().getOwner()));
        bookingResponse.setItem(booking.getItem());
        bookingResponse.setBooker(booking.getBooker());
        bookingResponse.setStatus(booking.getStatus());
        return bookingResponse;
    }
}
