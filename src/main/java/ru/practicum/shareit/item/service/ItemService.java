package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentRequest;
import ru.practicum.shareit.item.dto.CommentResponse;
import ru.practicum.shareit.item.dto.ItemControllerRequest;
import ru.practicum.shareit.item.dto.ItemControllerResponse;

import java.util.List;

public interface ItemService {
    ItemControllerResponse getItem(long userId, long itemId);

    List<ItemControllerResponse> getOwnerItems(long userId);

    List<ItemControllerResponse> searchItems(String text);

    ItemControllerResponse addItem(long userId, ItemControllerRequest item);

    CommentResponse addComment(long userId, long itemId, CommentRequest comment);

    ItemControllerResponse updateItem(long userId, long itemId, ItemControllerRequest item);

    ItemControllerResponse deleteItem(long userId, long itemId);
}
