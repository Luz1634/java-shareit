package ru.practicum.shareit.request.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.request.dto.ItemRequestRequest;
import ru.practicum.shareit.request.dto.ItemRequestResponse;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemRequestControllerTest {

    @Mock
    private ItemRequestService service;
    @InjectMocks
    private ItemRequestController controller;

    @Test
    void getItemRequest() {
        long userId = 1;
        long requestId = 1;
        ItemRequestResponse itemRequestResponse = new ItemRequestResponse();

        when(service.getItemRequest(userId, requestId)).thenReturn(itemRequestResponse);

        assertEquals(itemRequestResponse, controller.getItemRequest(userId, requestId));
    }

    @Test
    void getOwnerItemRequests() {
        long userId = 1;
        List<ItemRequestResponse> itemRequestResponse = List.of();

        when(service.getOwnerItemRequests(userId)).thenReturn(itemRequestResponse);

        assertEquals(itemRequestResponse, controller.getOwnerItemRequests(userId));
    }

    @Test
    void getItemRequests() {
        long userId = 1;
        int from = 0;
        int size = 1;
        List<ItemRequestResponse> itemRequestResponse = List.of();

        when(service.getItemRequests(userId, from, size)).thenReturn(itemRequestResponse);

        assertEquals(itemRequestResponse, controller.getItemRequests(userId, from, size));
    }

    @Test
    void addItemRequest() {
        long userId = 1;
        ItemRequestRequest itemRequestRequest = new ItemRequestRequest();
        ItemRequestResponse itemRequestResponse = new ItemRequestResponse();

        when(service.addItemRequest(userId, itemRequestRequest)).thenReturn(itemRequestResponse);

        assertEquals(itemRequestResponse, controller.addItemRequest(userId, itemRequestRequest));
    }
}