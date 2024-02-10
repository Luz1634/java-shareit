package ru.practicum.shareit.user.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dto.UserRequest;
import ru.practicum.shareit.user.dto.UserResponse;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService service;
    @InjectMocks
    private UserController controller;

    @Test
    void getUser() {
        UserResponse userResponse = new UserResponse();

        when(service.getUser(anyLong())).thenReturn(userResponse);

        assertEquals(userResponse, controller.getUser(1));
    }

    @Test
    void getAllUsers() {
        List<UserResponse> userResponses = List.of();

        when(service.getAllUsers()).thenReturn(userResponses);

        assertEquals(userResponses, controller.getAllUsers());
    }

    @Test
    void addUser() {
        UserRequest userRequest = new UserRequest();
        UserResponse userResponse = new UserResponse();

        when(service.addUser(userRequest)).thenReturn(userResponse);

        assertEquals(userResponse, controller.addUser(userRequest));
    }

    @Test
    void updateUser() {
        long userId = 1;
        UserRequest userRequest = new UserRequest();
        UserResponse userResponse = new UserResponse();

        when(service.updateUser(userId, userRequest)).thenReturn(userResponse);

        assertEquals(userResponse, controller.updateUser(userId, userRequest));
    }

    @Test
    void deleteUser() {
        long userId = 1;
        UserResponse userResponse = new UserResponse();

        when(service.deleteUser(userId)).thenReturn(userResponse);

        assertEquals(userResponse, controller.deleteUser(userId));
    }
}