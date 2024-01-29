package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.dto.ItemDb;

import java.util.List;

public interface ItemRepository extends JpaRepository<ItemDb, Long> {
    List<ItemDb> findByOwnerId(long ownerId);

    @Query(value = "SELECT * FROM items AS i " +
            "WHERE i.is_available = TRUE " +
            "AND (UPPER(i.name) LIKE UPPER(CONCAT('%', ?1, '%')) " +
            "OR UPPER(i.description) LIKE UPPER(CONCAT('%', ?1, '%')))",
            nativeQuery = true)
    List<ItemDb> search(String text);

    void deleteByOwnerId(long ownerId);
}
