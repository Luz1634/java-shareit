package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserStorage {
    public User getUser(long userId);

    public List<User> getAllUsers();

    public User addUser(User user);

    public User updateUser(User user);

    public User deleteUser(long userId);
}
