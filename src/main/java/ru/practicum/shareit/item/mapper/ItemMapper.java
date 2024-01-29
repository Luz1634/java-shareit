package ru.practicum.shareit.item.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemControllerRequest;
import ru.practicum.shareit.item.dto.ItemControllerResponse;
import ru.practicum.shareit.item.dto.ItemDb;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDb;
import ru.practicum.shareit.user.mapper.UserMapper;

@Component
@AllArgsConstructor
public class ItemMapper {

    private final UserMapper userMapper;

    public Item toItem(ItemControllerRequest itemControllerRequest) {
        Item item = new Item();
        item.setName(itemControllerRequest.getName());
        item.setDescription(itemControllerRequest.getDescription());
        item.setIsAvailable(itemControllerRequest.getAvailable());
        return item;
    }

    public ItemControllerResponse toItemControllerResponse(Item item) {
        ItemControllerResponse itemResponse = new ItemControllerResponse();
        itemResponse.setId(item.getId());
        itemResponse.setName(item.getName());
        itemResponse.setDescription(item.getDescription());
        itemResponse.setAvailable(item.getIsAvailable());
        return itemResponse;
    }

    public Item toItem(ItemDb itemDb) {
        Item item = new Item();
        item.setId(itemDb.getId());
        item.setName(itemDb.getName());
        item.setDescription(itemDb.getDescription());
        item.setIsAvailable(itemDb.getIsAvailable());
        return item;
    }

    public Item toItem(ItemDb itemDb, UserDb userDb) {
        Item item = new Item();
        item.setId(itemDb.getId());
        item.setName(itemDb.getName());
        item.setDescription(itemDb.getDescription());
        item.setIsAvailable(itemDb.getIsAvailable());
        item.setOwner(userMapper.toUser(itemDb.getOwner()));
        return item;
    }

    public ItemDb toItemDb(Item item) {
        ItemDb itemDb = new ItemDb();
        itemDb.setId(item.getId());
        itemDb.setName(item.getName());
        itemDb.setDescription(item.getDescription());
        itemDb.setIsAvailable(item.getIsAvailable());
        itemDb.setOwner(userMapper.toUserDb(item.getOwner()));
        return itemDb;
    }
}
