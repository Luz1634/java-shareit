package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.client.BookingClient;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.exception.model.UnsupportedStateException;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequestMapping(path = "/bookings")
@Slf4j
@RequiredArgsConstructor
@Validated
public class BookingController {

    private final BookingClient client;

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@Min(value = 1, message = "UserId должно быть больше 0")
                                             @RequestHeader("X-Sharer-User-Id") long userId,
                                             @Min(value = 1, message = "UserId должно быть больше 0")
                                             @PathVariable long bookingId) {
        log.info("GET запрос - getBooking, UserId: " + userId + ", BookingId: " + bookingId);
        return client.getBooking(userId, bookingId);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getOwnerBookings(@Min(value = 1, message = "UserId должно быть больше 0")
                                                   @RequestHeader("X-Sharer-User-Id") long userId,
                                                   @RequestParam(name = "state", defaultValue = "ALL") String stateString,
                                                   @Min(value = 0, message = "from должно быть больше или равно 0")
                                                   @RequestParam(defaultValue = "0") int from,
                                                   @Min(value = 1, message = "size должно быть больше 0")
                                                   @RequestParam(defaultValue = "20") int size) {
        log.info("GET запрос - getUserBookings, UserId: " + userId + ", State: " + stateString + ", from: " + from + ", size: " + size);

        BookingState state = BookingState.from(stateString)
                .orElseThrow(() -> new UnsupportedStateException("Unknown state: " + stateString));

        return client.getOwnerBookings(userId, state, from, size);
    }

    @GetMapping
    public ResponseEntity<Object> getUserBookings(@Min(value = 1, message = "UserId должно быть больше 0")
                                                  @RequestHeader("X-Sharer-User-Id") long userId,
                                                  @RequestParam(name = "state", defaultValue = "ALL") String stateString,
                                                  @Min(value = 0, message = "from должно быть больше или равно 0")
                                                  @RequestParam(defaultValue = "0") int from,
                                                  @Min(value = 1, message = "size должно быть больше 0")
                                                  @RequestParam(defaultValue = "20") int size) {
        log.info("GET запрос - getUserBookings, UserId: " + userId + ", State: " + stateString + ", from: " + from + ", size: " + size);

        BookingState state = BookingState.from(stateString)
                .orElseThrow(() -> new UnsupportedStateException("Unknown state: " + stateString));

        return client.getUserBookings(userId, state, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> addBooking(@Min(value = 1, message = "UserId должно быть больше 0")
                                             @RequestHeader("X-Sharer-User-Id") long userId,
                                             @Valid @RequestBody BookingRequest bookingRequest) {
        log.info("POST запрос - addBooking, UserId: " + userId +
                ", BookingRequest: " + bookingRequest.toString());
        return client.addBooking(userId, bookingRequest);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approveBooking(@Min(value = 1, message = "UserId должно быть больше 0")
                                                 @RequestHeader("X-Sharer-User-Id") long userId,
                                                 @Min(value = 1, message = "BookingId должно быть больше 0")
                                                 @PathVariable long bookingId,
                                                 @RequestParam("approved") Boolean isApprove) {
        log.info("PATCH запрос - approveBooking, UserId: " + userId +
                ", isApprove: " + isApprove.toString());
        return client.approveBooking(userId, bookingId, isApprove);
    }
}
