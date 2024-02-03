package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserRequest;
import ru.practicum.shareit.user.dto.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse getUser(long userId);

    List<UserResponse> getAllUsers();

    UserResponse addUser(UserRequest user);

    UserResponse updateUser(long userId, UserRequest user);

    UserResponse deleteUser(long userId);
}
