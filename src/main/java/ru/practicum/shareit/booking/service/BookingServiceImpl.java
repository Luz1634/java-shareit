package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.model.GetNonExistObjectException;
import ru.practicum.shareit.exception.model.NonOwnerAccessException;
import ru.practicum.shareit.exception.model.UnavailableObjectException;
import ru.practicum.shareit.exception.model.UnsupportedStateException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository repository;
    private final BookingMapper mapper;

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public BookingResponse getBooking(long userId, long bookingId) {
        Booking booking = repository.findById(bookingId)
                .orElseThrow(() -> new GetNonExistObjectException("Booking с заданным id = " + bookingId + " не найден"));
        if (booking.getBooker().getId() != userId && booking.getItem().getOwner().getId() != userId) {
            throw new NonOwnerAccessException("User с заданным id = " + userId + " не является владельцем или арендатором");
        }

        return mapper.toBookingResponse(booking);
    }

    @Override
    public List<BookingResponse> getUserBookings(long userId, String state) {
        userRepository.findById(userId)
                .orElseThrow(() -> new GetNonExistObjectException("User с заданным id = " + userId + " не найден"));

        List<Booking> bookings;

        switch (state) {
            case "ALL":
                bookings = repository.findByBookerIdOrderByStartDesc(userId);
                break;
            case "WAITING":
                bookings = repository.findByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING);
                break;
            case "REJECTED":
                bookings = repository.findByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED);
                break;
            case "PAST":
                bookings = repository.findByBookerIdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now());
                break;
            case "CURRENT":
                bookings = repository.findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(
                        userId,
                        LocalDateTime.now(),
                        LocalDateTime.now());
                break;
            case "FUTURE":
                bookings = repository.findByBookerIdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now());
                break;
            default:
                throw new UnsupportedStateException("Unknown state: " + state);
        }

        return bookings
                .stream()
                .map(mapper::toBookingResponse)
                .collect(toList());
    }

    @Override
    public List<BookingResponse> getOwnerBookings(long userId, String state) {
        if (itemRepository.findByOwnerId(userId).size() == 0) {
            throw new GetNonExistObjectException("User с заданным id = " + userId + " не найден или не имеет своих предметов");
        }

        List<Booking> bookings;

        switch (state) {
            case "ALL":
                bookings = repository.findByItem_OwnerIdOrderByStartDesc(userId);
                break;
            case "WAITING":
                bookings = repository.findByItem_OwnerIdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING);
                break;
            case "REJECTED":
                bookings = repository.findByItem_OwnerIdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED);
                break;
            case "PAST":
                bookings = repository.findByItem_OwnerIdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now());
                break;
            case "CURRENT":
                bookings = repository.findByItem_OwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(
                        userId,
                        LocalDateTime.now(),
                        LocalDateTime.now());
                break;
            case "FUTURE":
                bookings = repository.findByItem_OwnerIdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now());
                break;
            default:
                throw new UnsupportedStateException("Unknown state: " + state);
        }

        return bookings.stream()
                .map(mapper::toBookingResponse)
                .collect(toList());
    }

    @Override
    public BookingResponse addBooking(long userId, BookingRequest bookingRequest) {
        User booker = userRepository.findById(userId)
                .orElseThrow(() -> new GetNonExistObjectException("User с заданным id = " + userId + " не найден"));
        Item item = itemRepository.findById(bookingRequest.getItemId())
                .orElseThrow(() -> new GetNonExistObjectException("Item с заданным id = " + bookingRequest.getItemId() + " не найден"));
        if (item.getOwner().getId() == userId) {
            throw new GetNonExistObjectException("User с заданным id = " + userId + " является владельцем");
        }
        if (!item.getIsAvailable()) {
            throw new UnavailableObjectException("Item с заданным id = " + bookingRequest.getItemId() + " не доступен для бронирования");
        }

        Booking booking = mapper.toBooking(bookingRequest);

        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.WAITING);

        return mapper.toBookingResponse(repository.save(booking));
    }

    @Override
    public BookingResponse approveBooking(long userId, long bookingId, boolean isApprove) {
        Booking booking = repository.findById(bookingId)
                .orElseThrow(() -> new GetNonExistObjectException("Item с заданным id = " + bookingId + " не найден"));
        if (booking.getItem().getOwner().getId() != userId) {
            throw new NonOwnerAccessException("User с заданным id = " + userId + " не является владельцем");
        }

        if (isApprove) {
            if (booking.getStatus().equals(BookingStatus.APPROVED)) {
                throw new UnavailableObjectException("Booking с заданным id = " + bookingId + " уже имеет статус " + BookingStatus.APPROVED);
            }
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }

        return mapper.toBookingResponse(repository.save(booking));
    }
}
