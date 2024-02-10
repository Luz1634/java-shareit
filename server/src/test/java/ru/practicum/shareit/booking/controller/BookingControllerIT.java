package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.service.BookingService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.booking.model.BookingState.ALL;

@WebMvcTest(BookingController.class)
class BookingControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private BookingService service;

    @Test
    @SneakyThrows
    void getBooking_whenAllOk() {
        long userId = 1;
        long bookingId = 1;

        BookingResponse bookingResponse = new BookingResponse();

        when(service.getBooking(userId, bookingId)).thenReturn(bookingResponse);

        String response = mockMvc.perform(MockMvcRequestBuilders.get("/bookings/{bookingId}", bookingId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(response, objectMapper.writeValueAsString(bookingResponse));
    }

    @Test
    @SneakyThrows
    void getOwnerBookings_whenAllOk() {
        long userId = 1;
        BookingState state = ALL;
        int from = 0;
        int size = 1;

        List<BookingResponse> bookingResponses = List.of();

        when(service.getOwnerBookings(userId, state, from, size)).thenReturn(bookingResponses);

        String response = mockMvc.perform(MockMvcRequestBuilders.get("/bookings/owner")
                        .header("X-Sharer-User-Id", userId)
                        .param("from", from + "")
                        .param("size", size + ""))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(response, objectMapper.writeValueAsString(bookingResponses));
    }

    @Test
    @SneakyThrows
    void getUserBookings_whenAllOk() {
        long userId = 1;
        BookingState state = ALL;
        int from = 0;
        int size = 1;

        List<BookingResponse> bookingResponses = List.of();

        when(service.getUserBookings(userId, state, from, size)).thenReturn(bookingResponses);

        String response = mockMvc.perform(MockMvcRequestBuilders.get("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .param("from", from + "")
                        .param("size", size + ""))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(response, objectMapper.writeValueAsString(bookingResponses));
    }

    @Test
    @SneakyThrows
    void addBooking_whenAllOk() {
        long userId = 1;
        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setStart(LocalDateTime.of(3000, 1, 1, 1, 1, 1));
        bookingRequest.setEnd(LocalDateTime.of(3001, 1, 1, 1, 1, 1));
        bookingRequest.setItemId(1L);

        BookingResponse bookingResponse = new BookingResponse();

        when(service.addBooking(userId, bookingRequest)).thenReturn(bookingResponse);

        String response = mockMvc.perform(MockMvcRequestBuilders.post("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(bookingRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(response, objectMapper.writeValueAsString(bookingResponse));
    }

    @Test
    @SneakyThrows
    void approveBooking_whenAllOk() {
        long userId = 1;
        long bookingId = 1;
        boolean approved = true;

        BookingResponse bookingResponse = new BookingResponse();

        when(service.approveBooking(userId, bookingId, approved)).thenReturn(bookingResponse);

        String response = mockMvc.perform(MockMvcRequestBuilders.patch("/bookings/{bookingId}", bookingId)
                        .header("X-Sharer-User-Id", userId)
                        .param("approved", approved + ""))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(response, objectMapper.writeValueAsString(bookingResponse));
    }
}