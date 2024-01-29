package ru.practicum.shareit.user.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDb;
import ru.practicum.shareit.user.dto.UserRequest;
import ru.practicum.shareit.user.dto.UserResponse;
import ru.practicum.shareit.user.model.User;

@Component
public class UserMapper {

    public User toUser(UserRequest userRequest) {
        User user = new User();
        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());
        return user;
    }

    public UserResponse toUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setName(user.getName());
        userResponse.setEmail(user.getEmail());
        return userResponse;
    }

    public User toUser(UserDb userDb) {
        User user = new User();
        user.setId(userDb.getId());
        user.setName(userDb.getName());
        user.setEmail(userDb.getEmail());
        return user;
    }

    public UserDb toUserDb(User user) {
        UserDb userDb = new UserDb();
        userDb.setId(user.getId());
        userDb.setName(user.getName());
        userDb.setEmail(user.getEmail());
        return userDb;
    }
}
