package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.List;

public interface BookingService {
    BookingResponse getBooking(long userId, long bookingId);

    List<BookingResponse> getOwnerBookings(long userId, BookingState state, int from, int size);

    List<BookingResponse> getUserBookings(long userId, BookingState state, int from, int size);

    BookingResponse addBooking(long userId, BookingRequest booking);

    BookingResponse approveBooking(long userId, long bookingId, boolean isApprove);
}
