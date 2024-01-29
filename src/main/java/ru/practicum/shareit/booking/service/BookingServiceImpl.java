package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDb;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.model.GetNonExistObjectException;
import ru.practicum.shareit.exception.model.UnavailableObjectException;
import ru.practicum.shareit.exception.model.UnsupportedStateException;
import ru.practicum.shareit.item.dto.ItemDb;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.mapper.UserMapper;
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
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public Booking getBooking(long userId, long bookingId) {
        BookingDb bookingDb = repository.findById(bookingId)
                .orElseThrow(() -> new GetNonExistObjectException("Booking с заданным id = " + bookingId + " не найден"));
        if (bookingDb.getBooker().getId() != userId && bookingDb.getItem().getOwner().getId() != userId) {
            throw new GetNonExistObjectException("User с заданным id = " + userId + " не является владельцем или арендатором");
        }
        return mapper.toBooking(bookingDb);
    }

    @Override
    public List<Booking> getUserBooking(long userId, String state) {
        userRepository.findById(userId)
                .orElseThrow(() -> new GetNonExistObjectException("User с заданным id = " + userId + " не найден"));

        List<BookingDb> bookings;

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
                bookings = repository.findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId,
                        LocalDateTime.now(), LocalDateTime.now());
                break;
            case "FUTURE":
                bookings = repository.findByBookerIdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now());
                break;
            default:
                throw new UnsupportedStateException("Unknown state: " + state);
        }

        return bookings.stream()
                .map(mapper::toBooking)
                .collect(toList());
    }

    @Override
    public List<Booking> getOwnerBooking(long userId, String state) {
        if (itemRepository.findByOwnerId(userId).size() == 0) {
            throw new GetNonExistObjectException("User с заданным id = " + userId + " не найден или не имеет своих предметов");
        }

        List<BookingDb> bookings;

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
                bookings = repository.findByItem_OwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId, LocalDateTime.now(), LocalDateTime.now());
                break;
            case "FUTURE":
                bookings = repository.findByItem_OwnerIdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now());
                break;
            default:
                throw new UnsupportedStateException("Unknown state: " + state);
        }

        return bookings.stream()
                .map(mapper::toBooking)
                .collect(toList());
    }

    @Override
    public Booking addBooking(long userId, Booking booking) {
        User user = userMapper.toUser(userRepository.findById(userId)
                .orElseThrow(() -> new GetNonExistObjectException("User с заданным id = " + userId + " не найден")));

        ItemDb itemDb = itemRepository.findById(booking.getItem().getId())
                .orElseThrow(() -> new GetNonExistObjectException("Item с заданным id = " + booking.getItem().getId() + " не найден"));

        if (itemDb.getOwner().getId() == userId) {
            throw new GetNonExistObjectException("User с заданным id = " + userId + " является владельцем");
        }

        if (!itemDb.getIsAvailable()) {
            throw new UnavailableObjectException("Item с заданным id = " + booking.getItem().getId() + " не доступен для бронирования");
        }

        booking.setItem(itemMapper.toItem(itemDb));
        booking.setBooker(user);
        booking.getItem().setOwner(userMapper.toUser(itemDb.getOwner()));

        return mapper.toBooking(repository.save(mapper.toBookingDb(booking)));
    }

    @Override
    public Booking approveBooking(long userId, long bookingId, boolean idApprove) {
        BookingDb bookingDb = repository.findById(bookingId)
                .orElseThrow(() -> new GetNonExistObjectException("Item с заданным id = " + bookingId + " не найден"));
        if (bookingDb.getItem().getOwner().getId() != userId) {
            throw new GetNonExistObjectException("User с заданным id = " + userId + " не является владельцем");
        }

        if (idApprove) {
            if (bookingDb.getStatus().equals(BookingStatus.APPROVED)) {
                throw new UnavailableObjectException("Booking с заданным id = " + bookingId + " уже имеет статус " + BookingStatus.APPROVED);
            }
            bookingDb.setStatus(BookingStatus.APPROVED);
        } else {
            bookingDb.setStatus(BookingStatus.REJECTED);
        }

        return mapper.toBooking(repository.save(bookingDb));
    }
}
