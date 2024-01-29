package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.dto.CommentDb;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentDb, Long> {
    List<CommentDb> findByItemId(long itemId);
}
