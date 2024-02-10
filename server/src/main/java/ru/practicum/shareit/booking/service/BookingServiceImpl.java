package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
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
@Transactional(readOnly = true)
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
    public List<BookingResponse> getOwnerBookings(long userId, BookingState state, int from, int size) {
        itemRepository.findFirstByOwnerId(userId)
                .orElseThrow(() -> new GetNonExistObjectException("User с заданным id = " + userId + " не найден или не имеет своих предметов"));

        List<Booking> bookings;
        Pageable sortedByStart = PageRequest.of(from / size, size, Sort.by("start").descending());

        switch (state) {
            case ALL:
                bookings = repository.findByItem_OwnerId(userId, sortedByStart);
                break;
            case WAITING:
                bookings = repository.findByItem_OwnerIdAndStatus(userId, BookingStatus.WAITING, sortedByStart);
                break;
            case REJECTED:
                bookings = repository.findByItem_OwnerIdAndStatus(userId, BookingStatus.REJECTED, sortedByStart);
                break;
            case PAST:
                bookings = repository.findByItem_OwnerIdAndEndBefore(userId, LocalDateTime.now(), sortedByStart);
                break;
            case CURRENT:
                bookings = repository.findByItem_OwnerIdAndStartBeforeAndEndAfter(
                        userId,
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        sortedByStart);
                break;
            case FUTURE:
                bookings = repository.findByItem_OwnerIdAndStartAfter(userId, LocalDateTime.now(), sortedByStart);
                break;
            default:
                throw new UnsupportedStateException("Unknown state: " + state);
        }

        return bookings.stream()
                .map(mapper::toBookingResponse)
                .collect(toList());
    }

    @Override
    public List<BookingResponse> getUserBookings(long userId, BookingState state, int from, int size) {
        userRepository.findById(userId)
                .orElseThrow(() -> new GetNonExistObjectException("User с заданным id = " + userId + " не найден"));

        List<Booking> bookings;
        Pageable sortedByStart = PageRequest.of(from / size, size, Sort.by("start").descending());

        switch (state) {
            case ALL:
                bookings = repository.findByBookerId(userId, sortedByStart);
                break;
            case WAITING:
                bookings = repository.findByBookerIdAndStatus(userId, BookingStatus.WAITING, sortedByStart);
                break;
            case REJECTED:
                bookings = repository.findByBookerIdAndStatus(userId, BookingStatus.REJECTED, sortedByStart);
                break;
            case PAST:
                bookings = repository.findByBookerIdAndEndBefore(userId, LocalDateTime.now(), sortedByStart);
                break;
            case CURRENT:
                bookings = repository.findByBookerIdAndStartBeforeAndEndAfter(
                        userId,
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        sortedByStart);
                break;
            case FUTURE:
                bookings = repository.findByBookerIdAndStartAfter(userId, LocalDateTime.now(), sortedByStart);
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
    @Transactional
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
    @Transactional
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
