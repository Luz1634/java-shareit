package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    User getUser(long userId);

    List<User> getAllUsers();

    User addUser(User user);

    User updateUser(User user);

    User deleteUser(long userId);
}
