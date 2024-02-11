package ru.practicum.shareit.request.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemInfoForItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestRequest;
import ru.practicum.shareit.request.dto.ItemRequestResponse;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@Component
public class ItemRequestMapper {

    public ItemRequest toItemRequest(ItemRequestRequest itemRequestRequest) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(itemRequestRequest.getDescription());
        return itemRequest;
    }

    public ItemRequestResponse toItemRequestResponse(ItemRequest itemRequest) {
        ItemRequestResponse itemRequestResponse = new ItemRequestResponse();
        itemRequestResponse.setId(itemRequest.getId());
        itemRequestResponse.setDescription(itemRequest.getDescription());
        itemRequestResponse.setCreated(itemRequest.getCreated());
        return itemRequestResponse;
    }

    public ItemRequestResponse toItemRequestResponse(ItemRequest itemRequest, List<ItemInfoForItemRequest> items) {
        ItemRequestResponse itemRequestResponse = new ItemRequestResponse();
        itemRequestResponse.setId(itemRequest.getId());
        itemRequestResponse.setDescription(itemRequest.getDescription());
        itemRequestResponse.setCreated(itemRequest.getCreated());
        itemRequestResponse.setItems(items);
        return itemRequestResponse;
    }
}
