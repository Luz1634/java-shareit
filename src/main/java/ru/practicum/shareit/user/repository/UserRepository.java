package ru.practicum.shareit.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.dto.UserDb;

public interface UserRepository extends JpaRepository<UserDb, Long> {
    UserDb findByEmail(String email);
}
