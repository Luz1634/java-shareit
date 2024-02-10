package ru.practicum.shareit.item.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemControllerRequest;
import ru.practicum.shareit.item.dto.ItemControllerResponse;
import ru.practicum.shareit.item.dto.ItemInfoForItemRequest;
import ru.practicum.shareit.item.model.Item;

@Component
@AllArgsConstructor
public class ItemMapper {

    public Item toItem(ItemControllerRequest itemRequest) {
        Item item = new Item();
        item.setName(itemRequest.getName());
        item.setDescription(itemRequest.getDescription());
        item.setIsAvailable(itemRequest.getAvailable());
        return item;
    }

    public ItemControllerResponse toItemControllerResponse(Item item) {
        ItemControllerResponse itemResponse = new ItemControllerResponse();
        itemResponse.setId(item.getId());
        itemResponse.setName(item.getName());
        itemResponse.setDescription(item.getDescription());
        itemResponse.setAvailable(item.getIsAvailable());
        if (item.getRequest() != null) {
            itemResponse.setRequestId(item.getRequest().getId());
        }
        return itemResponse;
    }

    public ItemInfoForItemRequest toItemInfoForItemRequest(Item item, long requestId) {
        ItemInfoForItemRequest itemInfo = new ItemInfoForItemRequest();
        itemInfo.setId(item.getId());
        itemInfo.setName(item.getName());
        itemInfo.setDescription(item.getDescription());
        itemInfo.setAvailable(item.getIsAvailable());
        itemInfo.setRequestId(requestId);
        return itemInfo;
    }
}
