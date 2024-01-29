package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingService {
    Booking getBooking(long userId, long bookingId);

    List<Booking> getUserBooking(long userId, String state);

    List<Booking> getOwnerBooking(long userId, String state);

    Booking addBooking(long userId, Booking booking);

    Booking approveBooking(long userId, long bookingId, boolean idApprove);
}
