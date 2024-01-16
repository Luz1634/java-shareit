package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.dto.UserRequest;
import ru.practicum.shareit.user.dto.UserRequestUpdate;
import ru.practicum.shareit.user.dto.UserResponse;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping(path = "/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/{userId}")
    public UserResponse getUser(@Min(value = 1, message = "UserId должно быть больше 0") @PathVariable long userId) {
        log.info("GET запрос - getUser, userId:  " + userId);
        return userMapper.toUserResponse(userService.getUser(userId));
    }

    @GetMapping
    public List<UserResponse> getAllUsers() {
        log.info("GET запрос - getAllUsers");
        return userService.getAllUsers().stream()
                .map(userMapper::toUserResponse)
                .collect(toList());
    }

    @PostMapping
    public UserResponse addUser(@Valid @RequestBody UserRequest userRequest) {
        log.info("POST запрос - addUser, UserRequest: " + userRequest.toString());
        return userMapper.toUserResponse(userService.addUser(userMapper.toUser(userRequest)));
    }

    @PatchMapping("/{userId}")
    public UserResponse updateUser(@Valid @RequestBody UserRequestUpdate userRequest,
                                   @Min(value = 1, message = "UserId должно быть больше 0") @PathVariable long userId) {
        log.info("PATCH запрос - updateUser, userId: " + userId + ", UserRequest:  " + userRequest.toString());
        User user = userMapper.toUser(userRequest);
        user.setId(userId);
        return userMapper.toUserResponse(userService.updateUser(user));
    }

    @DeleteMapping("/{userId}")
    public UserResponse deleteUser(@Min(value = 1, message = "UserId должно быть больше 0") @PathVariable long userId) {
        log.info("DELETE запрос - deleteUser, userId: " + userId);
        return userMapper.toUserResponse(userService.deleteUser(userId));
    }
}
