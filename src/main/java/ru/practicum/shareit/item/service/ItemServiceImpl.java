package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.model.GetNonExistObjectException;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage;
    private final UserService userService;

    @Override
    public Item getItem(long itemId) {
        return itemStorage.getItem(itemId);
    }

    @Override
    public List<Item> getOwnerItems(long userId) {
        return userService.getUser(userId).getItems();
    }

    @Override
    public List<Item> searchItems(String text) {
        return itemStorage.searchItems(text);
    }

    @Override
    public Item addItem(Item item, long userId) {
        item.setOwner(userService.getUser(userId));
        item.getOwner().getItems().add(item);
        return itemStorage.addItem(item);
    }

    @Override
    public Item updateItem(Item item, long userId) {
        if (!userService.getUser(userId).equals(getItem(item.getId()).getOwner())) {
            throw new GetNonExistObjectException("указанный User: " + userId + " не является влалельцем Item: " + item.toString());
        }
        return itemStorage.updateItem(item);
    }

    @Override
    public Item deleteItem(long userId, long itemId) {
        if (getItem(itemId).getOwner().getId() != userId) {
            throw new GetNonExistObjectException("указанный User: " + userId + " не является влалельцем Item: " + getItem(itemId));
        }
        Item item = itemStorage.deleteItem(itemId);
        userService.getUser(item.getOwner().getId()).getItems().remove(item);
        return item;
    }
}
