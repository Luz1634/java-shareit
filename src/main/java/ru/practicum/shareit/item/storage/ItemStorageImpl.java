package ru.practicum.shareit.item.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.model.GetNonExistObjectException;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ItemStorageImpl implements ItemStorage {

    private final Map<Long, Item> items;
    private long lastId;

    @Override
    public Item getItem(long itemId) {
        checkExistItem(itemId);
        return items.get(itemId);
    }

    @Override
    public Item addItem(Item item) {
        item.setId(++lastId);
        items.put(lastId, item);
        return items.get(lastId);
    }

    @Override
    public List<Item> searchItems(String text) {
        String[] kewWords = text.toLowerCase().split(" ");
        List<Item> resultItems = new ArrayList<>();

        for (Item item : items.values()) {
            if (!item.getAvailable()) {
                continue;
            }

            String itemWords = (item.getName() + " " + item.getDescription()).toLowerCase();

            for (String keyWord : kewWords) {
                if (itemWords.contains(keyWord)) {
                    resultItems.add(item);
                    break;
                }
            }
        }

        return resultItems;
    }

    @Override
    public Item updateItem(Item item) {
        checkExistItem(item.getId());

        Item itemOld = items.get(item.getId());

        if (item.getName() != null) {
            itemOld.setName(item.getName());
        }

        if (item.getDescription() != null) {
            itemOld.setDescription(item.getDescription());
        }

        if (item.getAvailable() != null) {
            itemOld.setAvailable(item.getAvailable());
        }

        return itemOld;
    }

    @Override
    public Item deleteItem(long itemId) {
        checkExistItem(itemId);
        Item item = getItem(itemId);
        items.remove(itemId);
        return item;
    }

    private void checkExistItem(long itemId) {
        if (!items.containsKey(itemId)) {
            throw new GetNonExistObjectException("Item с заданным id = " + itemId + " не найден");
        }
    }
}
