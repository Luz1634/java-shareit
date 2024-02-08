package ru.practicum.shareit.item.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.dto.ItemControllerRequest;
import ru.practicum.shareit.item.dto.ItemControllerResponse;
import ru.practicum.shareit.item.dto.ItemInfoForItemRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ItemMapperTest {

    @InjectMocks
    private ItemMapper mapper;

    @Test
    void toItem() {
        ItemControllerRequest itemControllerRequest = new ItemControllerRequest();
        itemControllerRequest.setName("name");
        itemControllerRequest.setDescription("description");
        itemControllerRequest.setAvailable(true);

        Item item = new Item();
        item.setName("name");
        item.setDescription("description");
        item.setIsAvailable(true);

        assertEquals(item, mapper.toItem(itemControllerRequest));
    }

    @Test
    void toItemControllerResponse() {
        Item item = new Item();
        item.setId(1);
        item.setName("name");
        item.setDescription("description");
        item.setIsAvailable(true);
        item.setRequest(new ItemRequest(2, null, null, null));

        ItemControllerResponse itemControllerResponse = new ItemControllerResponse();
        itemControllerResponse.setId(1);
        itemControllerResponse.setName("name");
        itemControllerResponse.setDescription("description");
        itemControllerResponse.setAvailable(true);
        itemControllerResponse.setRequestId(2);

        assertEquals(itemControllerResponse, mapper.toItemControllerResponse(item));
    }

    @Test
    void toItemInfoForItemRequest() {
        Item item = new Item();
        item.setId(1);
        item.setName("name");
        item.setDescription("description");
        item.setIsAvailable(true);

        ItemInfoForItemRequest itemInfoForItemRequest = new ItemInfoForItemRequest();
        itemInfoForItemRequest.setId(1);
        itemInfoForItemRequest.setName("name");
        itemInfoForItemRequest.setDescription("description");
        itemInfoForItemRequest.setAvailable(true);
        itemInfoForItemRequest.setRequestId(2);

        assertEquals(itemInfoForItemRequest, mapper.toItemInfoForItemRequest(item, 2));
    }
}