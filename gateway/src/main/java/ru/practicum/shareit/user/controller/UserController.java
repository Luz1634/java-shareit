package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.client.UserClient;
import ru.practicum.shareit.user.dto.UserRequest;
import ru.practicum.shareit.validation.group.OnCreate;
import ru.practicum.shareit.validation.group.OnUpdate;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequestMapping(path = "/users")
@Slf4j
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserClient client;

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUser(@Min(value = 1, message = "UserId должно быть больше 0")
                                          @PathVariable long userId) {
        log.info("GET запрос - getUser, userId:  " + userId);
        return client.getUser(userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        log.info("GET запрос - getAllUsers");
        return client.getAllUsers();
    }

    @PostMapping
    @Validated(OnCreate.class)
    public ResponseEntity<Object> addUser(@Valid @RequestBody UserRequest userRequest) {
        log.info("POST запрос - addUser, UserRequest: " + userRequest.toString());
        return client.addUser(userRequest);
    }

    @PatchMapping("/{userId}")
    @Validated(OnUpdate.class)
    public ResponseEntity<Object> updateUser(@Min(value = 1, message = "UserId должно быть больше 0", groups = OnUpdate.class)
                                             @PathVariable long userId,
                                             @Valid @RequestBody UserRequest userRequest) {
        log.info("PATCH запрос - updateUser, userId: " + userId + ", UserRequest:  " + userRequest.toString());
        return client.updateUser(userId, userRequest);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@Min(value = 1, message = "UserId должно быть больше 0") @PathVariable long userId) {
        log.info("DELETE запрос - deleteUser, userId: " + userId);
        return client.deleteUser(userId);
    }
}
