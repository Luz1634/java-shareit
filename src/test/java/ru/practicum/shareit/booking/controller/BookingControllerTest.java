package ru.practicum.shareit.booking.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {

    @Mock
    private BookingService service;
    @InjectMocks
    private BookingController controller;

    @Test
    void getBooking() {
        BookingResponse bookingResponse = new BookingResponse();

        when(service.getBooking(anyLong(), anyLong())).thenReturn(bookingResponse);

        assertEquals(bookingResponse, controller.getBooking(1, 1));
    }

    @Test
    void getOwnerBookings() {
        List<BookingResponse> bookingResponse = List.of();

        when(service.getOwnerBookings(anyLong(), anyString(), anyInt(), anyInt())).thenReturn(bookingResponse);

        assertEquals(bookingResponse, controller.getOwnerBookings(1, "ALL", 0, 1));
    }

    @Test
    void getUserBookings() {
        List<BookingResponse> bookingResponse = List.of();

        when(service.getUserBookings(anyLong(), anyString(), anyInt(), anyInt())).thenReturn(bookingResponse);

        assertEquals(bookingResponse, controller.getUserBookings(1, "ALL", 0, 1));
    }

    @Test
    void addBooking() {
        long userId = 1;
        BookingRequest bookingRequest = new BookingRequest();
        BookingResponse bookingResponse = new BookingResponse();

        when(service.addBooking(userId, bookingRequest)).thenReturn(bookingResponse);

        assertEquals(bookingResponse, controller.addBooking(userId, bookingRequest));
    }

    @Test
    void approveBooking() {
        long userId = 1;
        long bookingId = 1;
        boolean isApprove = true;
        BookingResponse bookingResponse = new BookingResponse();

        when(service.approveBooking(userId, bookingId, isApprove)).thenReturn(bookingResponse);

        assertEquals(bookingResponse, controller.approveBooking(userId, bookingId, isApprove));
    }
}