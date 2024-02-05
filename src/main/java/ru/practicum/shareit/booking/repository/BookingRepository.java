package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBookerId(long userId, Pageable pageable);

    List<Booking> findByBookerIdAndStatus(long userId, BookingStatus status, Pageable pageable);

    List<Booking> findByBookerIdAndEndBefore(long userId, LocalDateTime end, Pageable pageable);

    List<Booking> findByBookerIdAndStartBeforeAndEndAfter(long userId, LocalDateTime start, LocalDateTime end, Pageable pageable);

    List<Booking> findByBookerIdAndStartAfter(long userId, LocalDateTime start, Pageable pageable);

    List<Booking> findByItem_OwnerId(long userId, Pageable pageable);

    List<Booking> findByItem_OwnerIdAndStatus(long userId, BookingStatus status, Pageable pageable);

    List<Booking> findByItem_OwnerIdAndEndBefore(long userId, LocalDateTime end, Pageable pageable);

    List<Booking> findByItem_OwnerIdAndStartBeforeAndEndAfter(long userId, LocalDateTime start, LocalDateTime end, Pageable pageable);

    List<Booking> findByItem_OwnerIdAndStartAfter(long userId, LocalDateTime start, Pageable pageable);

    Booking findFirstByItemIdAndItem_OwnerIdAndStartBeforeAndStatusOrderByStartDesc(long itemId, long userId, LocalDateTime end, BookingStatus status);

    Booking findFirstByItemIdAndItem_OwnerIdAndStartAfterAndStatusOrderByStartAsc(long itemId, long userId, LocalDateTime start, BookingStatus status);

    Optional<Booking> findFirstByBookerIdAndItemIdAndEndBefore(long bookerId, long itemId, LocalDateTime end);
}
