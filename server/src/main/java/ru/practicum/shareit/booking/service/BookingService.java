package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.booking.dto.BookingResponse;

import java.util.List;

public interface BookingService {
    BookingResponse getBooking(long userId, long bookingId);

    List<BookingResponse> getOwnerBookings(long userId, String state, int from, int size);

    List<BookingResponse> getUserBookings(long userId, String state, int from, int size);

    BookingResponse addBooking(long userId, BookingRequest booking);

    BookingResponse approveBooking(long userId, long bookingId, boolean isApprove);
}
