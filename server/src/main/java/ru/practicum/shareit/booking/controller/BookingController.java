package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.model.UnsupportedStateException;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@Slf4j
@RequiredArgsConstructor
public class BookingController {

    private final BookingService service;

    @GetMapping("/{bookingId}")
    public BookingResponse getBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                      @PathVariable long bookingId) {
        log.info("GET запрос - getBooking, UserId: " + userId + ", BookingId: " + bookingId);
        return service.getBooking(userId, bookingId);
    }

    @GetMapping("/owner")
    public List<BookingResponse> getOwnerBookings(@RequestHeader("X-Sharer-User-Id") long userId,
                                                  @RequestParam(name = "state", defaultValue = "ALL") String stateString,
                                                  @RequestParam(defaultValue = "0") int from,
                                                  @RequestParam(defaultValue = "20") int size) {
        log.info("GET запрос - getUserBookings, UserId: " + userId + ", State: " + stateString + ", from: " + from + ", size: " + size);
        BookingState state = BookingState.from(stateString)
                .orElseThrow(() -> new UnsupportedStateException("Unknown state: " + stateString));
        return service.getOwnerBookings(userId, state, from, size);
    }

    @GetMapping
    public List<BookingResponse> getUserBookings(@RequestHeader("X-Sharer-User-Id") long userId,
                                                 @RequestParam(name = "state", defaultValue = "ALL") String stateString,
                                                 @RequestParam(defaultValue = "0") int from,
                                                 @RequestParam(defaultValue = "20") int size) {
        log.info("GET запрос - getUserBookings, UserId: " + userId + ", State: " + stateString + ", from: " + from + ", size: " + size);
        BookingState state = BookingState.from(stateString)
                .orElseThrow(() -> new UnsupportedStateException("Unknown state: " + stateString));
        return service.getUserBookings(userId, state, from, size);
    }

    @PostMapping
    public BookingResponse addBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                      @RequestBody BookingRequest bookingRequest) {
        log.info("POST запрос - addBooking, UserId: " + userId +
                ", BookingRequest: " + bookingRequest.toString());
        return service.addBooking(userId, bookingRequest);
    }

    @PatchMapping("/{bookingId}")
    public BookingResponse approveBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                          @PathVariable long bookingId,
                                          @RequestParam("approved") Boolean isApprove) {
        log.info("PATCH запрос - approveBooking, UserId: " + userId +
                ", isApprove: " + isApprove);
        return service.approveBooking(userId, bookingId, isApprove);
    }
}
