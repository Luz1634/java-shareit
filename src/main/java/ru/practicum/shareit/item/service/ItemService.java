package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentRequest;
import ru.practicum.shareit.item.dto.ItemControllerResponse;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    ItemControllerResponse getItem(long userId, long itemId);

    List<ItemControllerResponse> getOwnerItems(long userId);

    List<Item> searchItems(String text);

    Item addItem(Item item, long userId);

    Comment addComment(long userId, long itemId, CommentRequest comment);

    Item updateItem(Item item, long userId);

    Item deleteItem(long userId, long itemId);
}
