package ru.practicum.shareit.booking.repository;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class BookingRepositoryTest {

    @Test
    void findByBookerId() {
    }

    @Test
    void findByBookerIdAndStatus() {
    }

    @Test
    void findByBookerIdAndEndBefore() {
    }

    @Test
    void findByBookerIdAndStartBeforeAndEndAfter() {
    }

    @Test
    void findByBookerIdAndStartAfter() {
    }

    @Test
    void findByItem_OwnerId() {
    }

    @Test
    void findByItem_OwnerIdAndStatus() {
    }

    @Test
    void findByItem_OwnerIdAndEndBefore() {
    }

    @Test
    void findByItem_OwnerIdAndStartBeforeAndEndAfter() {
    }

    @Test
    void findByItem_OwnerIdAndStartAfter() {
    }

    @Test
    void findFirstByItemIdAndItem_OwnerIdAndStartBeforeAndStatusOrderByStartDesc() {
    }

    @Test
    void findFirstByItemIdAndItem_OwnerIdAndStartAfterAndStatusOrderByStartAsc() {
    }

    @Test
    void findFirstByBookerIdAndItemIdAndEndBefore() {
    }
}