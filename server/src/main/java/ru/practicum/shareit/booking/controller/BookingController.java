package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@Slf4j
@RequiredArgsConstructor
@Validated
public class BookingController {

    private final BookingService service;

    @GetMapping("/{bookingId}")
    public BookingResponse getBooking(@Min(value = 1, message = "UserId должно быть больше 0")
                                      @RequestHeader("X-Sharer-User-Id") long userId,
                                      @Min(value = 1, message = "UserId должно быть больше 0")
                                      @PathVariable long bookingId) {
        log.info("GET запрос - getBooking, UserId: " + userId + ", BookingId: " + bookingId);
        return service.getBooking(userId, bookingId);
    }

    @GetMapping("/owner")
    public List<BookingResponse> getOwnerBookings(@Min(value = 1, message = "UserId должно быть больше 0")
                                                  @RequestHeader("X-Sharer-User-Id") long userId,
                                                  @RequestParam(defaultValue = "ALL") String state,
                                                  @Min(value = 0, message = "from должно быть больше или равно 0")
                                                  @RequestParam(defaultValue = "0") int from,
                                                  @Min(value = 1, message = "size должно быть больше 0")
                                                  @RequestParam(defaultValue = "20") int size) {
        log.info("GET запрос - getUserBookings, UserId: " + userId + ", State: " + state + ", from: " + from + ", size: " + size);
        return service.getOwnerBookings(userId, state, from, size);
    }

    @GetMapping
    public List<BookingResponse> getUserBookings(@Min(value = 1, message = "UserId должно быть больше 0")
                                                 @RequestHeader("X-Sharer-User-Id") long userId,
                                                 @RequestParam(defaultValue = "ALL") String state,
                                                 @Min(value = 0, message = "from должно быть больше или равно 0")
                                                 @RequestParam(defaultValue = "0") int from,
                                                 @Min(value = 1, message = "size должно быть больше 0")
                                                 @RequestParam(defaultValue = "20") int size) {
        log.info("GET запрос - getUserBookings, UserId: " + userId + ", State: " + state + ", from: " + from + ", size: " + size);
        return service.getUserBookings(userId, state, from, size);
    }

    @PostMapping
    public BookingResponse addBooking(@Min(value = 1, message = "UserId должно быть больше 0")
                                      @RequestHeader("X-Sharer-User-Id") long userId,
                                      @Valid @RequestBody BookingRequest bookingRequest) {
        log.info("POST запрос - addBooking, UserId: " + userId +
                ", BookingRequest: " + bookingRequest.toString());
        return service.addBooking(userId, bookingRequest);
    }

    @PatchMapping("/{bookingId}")
    public BookingResponse approveBooking(@Min(value = 1, message = "UserId должно быть больше 0")
                                          @RequestHeader("X-Sharer-User-Id") long userId,
                                          @Min(value = 1, message = "UserId должно быть больше 0")
                                          @PathVariable long bookingId,
                                          @RequestParam("approved") Boolean isApprove) {
        log.info("PATCH запрос - approveBooking, UserId: " + userId +
                ", isApprove: " + isApprove.toString());
        return service.approveBooking(userId, bookingId, isApprove);
    }
}
