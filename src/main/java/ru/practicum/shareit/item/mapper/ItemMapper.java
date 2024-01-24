package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.dto.ItemControllerRequest;
import ru.practicum.shareit.item.dto.ItemControllerResponse;

@Component
public class ItemMapper {

    public Item toItem(ItemControllerRequest itemControllerRequest) {
        Item item = new Item();
        item.setName(itemControllerRequest.getName());
        item.setDescription(itemControllerRequest.getDescription());
        item.setAvailable(itemControllerRequest.getAvailable());
        return item;
    }

    public ItemControllerResponse toItemControllerResponse(Item item) {
        ItemControllerResponse itemControllerResponse = new ItemControllerResponse();
        itemControllerResponse.setId(item.getId());
        itemControllerResponse.setName(item.getName());
        itemControllerResponse.setDescription(item.getDescription());
        itemControllerResponse.setAvailable(item.getAvailable());
        return itemControllerResponse;
    }
}
