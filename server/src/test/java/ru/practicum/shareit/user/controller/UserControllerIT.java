package ru.practicum.shareit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.user.dto.UserRequest;
import ru.practicum.shareit.user.dto.UserResponse;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserService service;

    @Test
    @SneakyThrows
    void getUser_whenAllOk() {
        UserResponse userResponse = new UserResponse();

        when(service.getUser(anyLong())).thenReturn(userResponse);

        String response = mockMvc.perform(MockMvcRequestBuilders.get("/users/{userId}", 1))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(response, objectMapper.writeValueAsString(userResponse));
    }

    @Test
    @SneakyThrows
    void getAllUsers_whenAllOk() {
        List<UserResponse> userResponses = List.of();

        when(service.getAllUsers()).thenReturn(userResponses);

        String response = mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(response, objectMapper.writeValueAsString(userResponses));
    }

    @Test
    @SneakyThrows
    void addUser_whenAllOk() {
        UserRequest userRequest = new UserRequest();
        userRequest.setName("name");
        userRequest.setEmail("user@email.com");

        UserResponse userResponse = new UserResponse();

        when(service.addUser(userRequest)).thenReturn(userResponse);

        String response = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(response, objectMapper.writeValueAsString(userResponse));
    }

    @Test
    @SneakyThrows
    void updateUser_whenAllOk() {
        long userId = 1;
        UserRequest userRequest = new UserRequest();
        userRequest.setName("name");
        userRequest.setEmail("user@email.com");

        UserResponse userResponse = new UserResponse();

        when(service.updateUser(userId, userRequest)).thenReturn(userResponse);

        String response = mockMvc.perform(MockMvcRequestBuilders.patch("/users/{userId}", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(response, objectMapper.writeValueAsString(userResponse));
    }

    @Test
    @SneakyThrows
    void deleteUser_whenAllOk() {
        long userId = 1;
        UserResponse userResponse = new UserResponse();

        when(service.deleteUser(userId)).thenReturn(userResponse);

        String response = mockMvc.perform(MockMvcRequestBuilders.delete("/users/{userId}", userId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(response, objectMapper.writeValueAsString(userResponse));
    }
}