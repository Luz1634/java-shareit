package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {
    public Item getItem(long itemId);

    public Item addItem(Item item);

    public List<Item> searchItems(String text);

    public Item updateItem(Item item);

    public Item deleteItem(long itemId);
}
