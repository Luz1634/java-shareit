package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestRequest;
import ru.practicum.shareit.request.dto.ItemRequestResponse;

import java.util.List;

public interface ItemRequestService {
    ItemRequestResponse getItemRequest(long userId, long requestId);

    List<ItemRequestResponse> getOwnerItemRequests(long userId);

    List<ItemRequestResponse> getItemRequests(long userId, int from, int size);

    ItemRequestResponse addItemRequest(long userId, ItemRequestRequest itemRequestRequest);
}
