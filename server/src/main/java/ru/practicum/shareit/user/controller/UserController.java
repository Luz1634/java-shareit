package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserRequest;
import ru.practicum.shareit.user.dto.UserResponse;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping("/{userId}")
    public UserResponse getUser(@PathVariable long userId) {
        log.info("GET запрос - getUser, userId:  " + userId);
        return service.getUser(userId);
    }

    @GetMapping
    public List<UserResponse> getAllUsers() {
        log.info("GET запрос - getAllUsers");
        return service.getAllUsers();
    }

    @PostMapping
    public UserResponse addUser(@RequestBody UserRequest userRequest) {
        log.info("POST запрос - addUser, UserRequest: " + userRequest.toString());
        return service.addUser(userRequest);
    }

    @PatchMapping("/{userId}")
    public UserResponse updateUser(@PathVariable long userId,
                                   @RequestBody UserRequest userRequest) {
        log.info("PATCH запрос - updateUser, userId: " + userId + ", UserRequest:  " + userRequest.toString());
        return service.updateUser(userId, userRequest);
    }

    @DeleteMapping("/{userId}")
    public UserResponse deleteUser(@PathVariable long userId) {
        log.info("DELETE запрос - deleteUser, userId: " + userId);
        return service.deleteUser(userId);
    }
}
