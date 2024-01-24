package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    public Item getItem(long itemId);

    public List<Item> getOwnerItems(long userId);

    public List<Item> searchItems(String text);

    public Item addItem(Item item, long userId);

    public Item updateItem(Item item, long userId);

    public Item deleteItem(long userId, long itemId);
}
