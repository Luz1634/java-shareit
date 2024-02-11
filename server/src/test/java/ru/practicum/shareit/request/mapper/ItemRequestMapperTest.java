package ru.practicum.shareit.request.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.request.dto.ItemRequestRequest;
import ru.practicum.shareit.request.dto.ItemRequestResponse;
import ru.practicum.shareit.request.model.ItemRequest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ItemRequestMapperTest {

    @InjectMocks
    private ItemRequestMapper mapper;

    @Test
    void toItemRequest() {
        ItemRequestRequest itemRequestRequest = new ItemRequestRequest();
        itemRequestRequest.setDescription("description");

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription("description");

        assertEquals(itemRequest, mapper.toItemRequest(itemRequestRequest));
    }

    @Test
    void toItemRequestResponse() {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1);
        itemRequest.setDescription("description");
        itemRequest.setCreated(LocalDateTime.now());

        ItemRequestResponse itemRequestResponse = new ItemRequestResponse();
        itemRequestResponse.setId(1);
        itemRequestResponse.setDescription("description");
        itemRequestResponse.setCreated(itemRequest.getCreated());

        assertEquals(itemRequestResponse, mapper.toItemRequestResponse(itemRequest));
    }

    @Test
    void toItemRequestResponseWithItems() {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1);
        itemRequest.setDescription("description");
        itemRequest.setCreated(LocalDateTime.now());

        ItemRequestResponse itemRequestResponse = new ItemRequestResponse();
        itemRequestResponse.setId(1);
        itemRequestResponse.setDescription("description");
        itemRequestResponse.setCreated(itemRequest.getCreated());
        itemRequestResponse.setItems(List.of());

        assertEquals(itemRequestResponse, mapper.toItemRequestResponse(itemRequest, List.of()));
    }
}