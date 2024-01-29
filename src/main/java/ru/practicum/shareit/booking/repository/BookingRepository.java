package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.dto.BookingDb;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<BookingDb, Long> {
    List<BookingDb> findByBookerIdOrderByStartDesc(long userId);

    List<BookingDb> findByBookerIdAndStatusOrderByStartDesc(long userId, BookingStatus status);

    List<BookingDb> findByBookerIdAndEndBeforeOrderByStartDesc(long userId, LocalDateTime end);

    List<BookingDb> findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(long userId, LocalDateTime start, LocalDateTime end);

    List<BookingDb> findByBookerIdAndStartAfterOrderByStartDesc(long userId, LocalDateTime start);

    List<BookingDb> findByItem_OwnerIdOrderByStartDesc(long userId);

    List<BookingDb> findByItem_OwnerIdAndStatusOrderByStartDesc(long userId, BookingStatus status);

    List<BookingDb> findByItem_OwnerIdAndEndBeforeOrderByStartDesc(long userId, LocalDateTime end);

    List<BookingDb> findByItem_OwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(long userId, LocalDateTime start, LocalDateTime end);

    List<BookingDb> findByItem_OwnerIdAndStartAfterOrderByStartDesc(long userId, LocalDateTime start);

    BookingDb findFirstByItemIdAndItem_OwnerIdAndStartBeforeAndStatusOrderByStartDesc(long itemId, long userId, LocalDateTime end, BookingStatus status);

    BookingDb findFirstByItemIdAndItem_OwnerIdAndStartAfterAndStatusOrderByStartAsc(long itemId, long userId, LocalDateTime start, BookingStatus status);

    Optional<BookingDb> findFirstByBookerIdAndItemIdAndEndBefore(long bookerId, long itemId, LocalDateTime end);
}
