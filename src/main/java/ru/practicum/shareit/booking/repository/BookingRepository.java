package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBookerIdOrderByStartDesc(long userId);

    List<Booking> findByBookerIdAndStatusOrderByStartDesc(long userId, BookingStatus status);

    List<Booking> findByBookerIdAndEndBeforeOrderByStartDesc(long userId, LocalDateTime end);

    List<Booking> findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(long userId, LocalDateTime start, LocalDateTime end);

    List<Booking> findByBookerIdAndStartAfterOrderByStartDesc(long userId, LocalDateTime start);

    List<Booking> findByItem_OwnerIdOrderByStartDesc(long userId);

    List<Booking> findByItem_OwnerIdAndStatusOrderByStartDesc(long userId, BookingStatus status);

    List<Booking> findByItem_OwnerIdAndEndBeforeOrderByStartDesc(long userId, LocalDateTime end);

    List<Booking> findByItem_OwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(long userId, LocalDateTime start, LocalDateTime end);

    List<Booking> findByItem_OwnerIdAndStartAfterOrderByStartDesc(long userId, LocalDateTime start);

    Booking findFirstByItemIdAndItem_OwnerIdAndStartBeforeAndStatusOrderByStartDesc(long itemId, long userId, LocalDateTime end, BookingStatus status);

    Booking findFirstByItemIdAndItem_OwnerIdAndStartAfterAndStatusOrderByStartAsc(long itemId, long userId, LocalDateTime start, BookingStatus status);

    Optional<Booking> findFirstByBookerIdAndItemIdAndEndBefore(long bookerId, long itemId, LocalDateTime end);
}
