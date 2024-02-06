package ru.practicum.shareit.request.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.request.dto.ItemRequestRequest;
import ru.practicum.shareit.request.dto.ItemRequestResponse;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
class ItemRequestControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ItemRequestService service;

    @Test
    @SneakyThrows
    void getItemRequest_whenAllOk() {
        long userId = 1;
        long requestId = 1;

        ItemRequestResponse itemRequestResponse = new ItemRequestResponse();

        when(service.getItemRequest(userId, requestId)).thenReturn(itemRequestResponse);

        String response = mockMvc.perform(MockMvcRequestBuilders.get("/requests/{requestId}", requestId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(response, objectMapper.writeValueAsString(itemRequestResponse));
    }

    @Test
    @SneakyThrows
    void getItemRequest_whenIdIsNotValid() {
        long userId = -1;
        long requestId = -1;

        mockMvc.perform(MockMvcRequestBuilders.get("/requests/{requestId}", requestId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isBadRequest());

        verify(service, never()).getItemRequest(userId, requestId);
    }

    @Test
    @SneakyThrows
    void getOwnerItemRequests_whenAllOk() {
        long userId = 1;

        List<ItemRequestResponse> itemRequestResponse = List.of();

        when(service.getOwnerItemRequests(userId)).thenReturn(itemRequestResponse);

        String response = mockMvc.perform(MockMvcRequestBuilders.get("/requests")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(response, objectMapper.writeValueAsString(itemRequestResponse));
    }

    @Test
    @SneakyThrows
    void getOwnerItemRequests_whenIdIsNotValid() {
        long userId = -1;

        mockMvc.perform(MockMvcRequestBuilders.get("/requests")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isBadRequest());

        verify(service, never()).getOwnerItemRequests(userId);
    }

    @Test
    @SneakyThrows
    void getItemRequests_whenAllOk() {
        long userId = 1;
        int from = 0;
        int size = 1;

        List<ItemRequestResponse> itemRequestResponse = List.of();

        when(service.getItemRequests(userId, from, size)).thenReturn(itemRequestResponse);

        String response = mockMvc.perform(MockMvcRequestBuilders.get("/requests/all")
                        .header("X-Sharer-User-Id", userId)
                        .param("from", from + "")
                        .param("size", size + ""))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(response, objectMapper.writeValueAsString(itemRequestResponse));
    }

    @Test
    @SneakyThrows
    void getItemRequests_whenIdIsNotValid() {
        long userId = -1;
        int from = -1;
        int size = -1;

        mockMvc.perform(MockMvcRequestBuilders.get("/requests/all")
                        .header("X-Sharer-User-Id", userId)
                        .param("from", from + "")
                        .param("size", size + ""))
                .andExpect(status().isBadRequest());

        verify(service, never()).getItemRequests(userId, from, size);
    }

    @Test
    @SneakyThrows
    void addItemRequest_whenAllOk() {
        long userId = 1;
        ItemRequestRequest itemRequestRequest = new ItemRequestRequest();
        itemRequestRequest.setDescription("description");

        ItemRequestResponse itemRequestResponse = new ItemRequestResponse();

        when(service.addItemRequest(userId, itemRequestRequest)).thenReturn(itemRequestResponse);

        String response = mockMvc.perform(MockMvcRequestBuilders.post("/requests")
                        .header("X-Sharer-User-Id", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemRequestRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(response, objectMapper.writeValueAsString(itemRequestResponse));
    }

    @Test
    @SneakyThrows
    void addItemRequest_whenIdAndBodyIsNotValid() {
        long userId = -1;
        ItemRequestRequest itemRequestRequest = new ItemRequestRequest();
        itemRequestRequest.setDescription("");

        mockMvc.perform(MockMvcRequestBuilders.post("/requests")
                        .header("X-Sharer-User-Id", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemRequestRequest)))
                .andExpect(status().isBadRequest());

        verify(service, never()).addItemRequest(userId, itemRequestRequest);
    }
}