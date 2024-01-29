package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping(path = "/bookings")
@Slf4j
@RequiredArgsConstructor
@Validated
public class BookingController {

    private final BookingService service;
    private final BookingMapper mapper;

    @GetMapping("/{bookingId}")
    public BookingResponse getBooking(@Min(value = 1, message = "UserId должно быть больше 0")
                                      @RequestHeader("X-Sharer-User-Id") long userId,
                                      @Min(value = 1, message = "UserId должно быть больше 0")
                                      @PathVariable long bookingId) {
        log.info("GET запрос - getBooking, UserId: " + userId + ", BookingId: " + bookingId);
        return mapper.toBookingResponse(service.getBooking(userId, bookingId));
    }

    @GetMapping
    public List<BookingResponse> getUserBookings(@Min(value = 1, message = "UserId должно быть больше 0")
                                                 @RequestHeader("X-Sharer-User-Id") long userId,
                                                 @RequestParam(defaultValue = "ALL") String state) {
        log.info("GET запрос - getUserBookings, UserId: " + userId + ", State: " + state);
        return service.getUserBooking(userId, state).stream()
                .map(mapper::toBookingResponse)
                .collect(toList());
    }

    @GetMapping("/owner")
    public List<BookingResponse> getOwnerBooking(@Min(value = 1, message = "UserId должно быть больше 0")
                                                 @RequestHeader("X-Sharer-User-Id") long userId,
                                                 @RequestParam(defaultValue = "ALL") String state) {
        log.info("GET запрос - getUserBookings, UserId: " + userId + ", State: " + state);
        return service.getOwnerBooking(userId, state).stream()
                .map(mapper::toBookingResponse)
                .collect(toList());
    }

    @PostMapping
    public BookingResponse addBooking(@Min(value = 1, message = "UserId должно быть больше 0")
                                      @RequestHeader("X-Sharer-User-Id") long userId,
                                      @Valid @RequestBody BookingRequest bookingRequest) {
        log.info("POST запрос - addBooking, UserId: " + userId +
                ", BookingRequest: " + bookingRequest.toString());
        Booking booking = mapper.toBooking(bookingRequest);
        booking.setStatus(BookingStatus.WAITING);
        return mapper.toBookingResponse(service.addBooking(userId, booking));
    }

    @PatchMapping("/{bookingId}")
    public BookingResponse approveBooking(@Min(value = 1, message = "UserId должно быть больше 0")
                                          @RequestHeader("X-Sharer-User-Id") long userId,
                                          @Min(value = 1, message = "UserId должно быть больше 0")
                                          @PathVariable long bookingId,
                                          @RequestParam("approved") Boolean isApprove) {
        log.info("PATCH запрос - approveBooking, UserId: " + userId +
                ", isApprove: " + isApprove.toString());
        return mapper.toBookingResponse(service.approveBooking(userId, bookingId, isApprove));
    }
}
