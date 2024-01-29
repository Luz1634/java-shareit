package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserRequest;
import ru.practicum.shareit.user.dto.UserResponse;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.validation.group.OnCreate;
import ru.practicum.shareit.validation.group.OnUpdate;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping(path = "/users")
@Slf4j
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService service;
    private final UserMapper mapper;

    @GetMapping("/{userId}")
    public UserResponse getUser(@Min(value = 1, message = "UserId должно быть больше 0") @PathVariable long userId) {
        log.info("GET запрос - getUser, userId:  " + userId);
        return mapper.toUserResponse(service.getUser(userId));
    }

    @GetMapping
    public List<UserResponse> getAllUsers() {
        log.info("GET запрос - getAllUsers");
        return service.getAllUsers().stream()
                .map(mapper::toUserResponse)
                .collect(toList());
    }

    @PostMapping
    @Validated(OnCreate.class)
    public UserResponse addUser(@Valid @RequestBody UserRequest userRequest) {
        log.info("POST запрос - addUser, UserRequest: " + userRequest.toString());
        return mapper.toUserResponse(service.addUser(mapper.toUser(userRequest)));
    }

    @PatchMapping("/{userId}")
    @Validated(OnUpdate.class)
    public UserResponse updateUser(@Valid @RequestBody UserRequest userRequest,
                                   @Min(value = 1, message = "UserId должно быть больше 0") @PathVariable long userId) {
        log.info("PATCH запрос - updateUser, userId: " + userId + ", UserRequest:  " + userRequest.toString());
        User user = mapper.toUser(userRequest);
        user.setId(userId);
        return mapper.toUserResponse(service.updateUser(user));
    }

    @DeleteMapping("/{userId}")
    public UserResponse deleteUser(@Min(value = 1, message = "UserId должно быть больше 0") @PathVariable long userId) {
        log.info("DELETE запрос - deleteUser, userId: " + userId);
        return mapper.toUserResponse(service.deleteUser(userId));
    }
}
