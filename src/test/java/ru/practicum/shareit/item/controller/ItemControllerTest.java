package ru.practicum.shareit.item.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.dto.CommentRequest;
import ru.practicum.shareit.item.dto.CommentResponse;
import ru.practicum.shareit.item.dto.ItemControllerRequest;
import ru.practicum.shareit.item.dto.ItemControllerResponse;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemControllerTest {

    @Mock
    private ItemService service;
    @InjectMocks
    private ItemController controller;

    @Test
    void getItem() {
        ItemControllerResponse itemControllerResponse = new ItemControllerResponse();

        when(service.getItem(anyLong(), anyLong())).thenReturn(itemControllerResponse);

        assertEquals(itemControllerResponse, controller.getItem(1, 1));
    }

    @Test
    void getOwnerItems() {
        long userId = 1;
        int from = 0;
        int size = 1;
        List<ItemControllerResponse> itemControllerResponse = List.of();

        when(service.getOwnerItems(userId, from, size)).thenReturn(itemControllerResponse);

        assertEquals(itemControllerResponse, controller.getOwnerItems(userId, from, size));
    }

    @Test
    void searchItems_whenTextExist() {
        long userId = 1;
        String text = "text";
        int from = 0;
        int size = 1;
        List<ItemControllerResponse> itemControllerResponse = List.of();

        when(service.searchItems(text, from, size)).thenReturn(itemControllerResponse);

        assertEquals(itemControllerResponse, controller.searchItems(userId, text, from, size));
    }

    @Test
    void searchItems_whenTextNonExist() {
        long userId = 1;
        String text = null;
        int from = 0;
        int size = 1;
        List<ItemControllerResponse> itemControllerResponse = List.of();

        assertEquals(itemControllerResponse, controller.searchItems(userId, text, from, size));
    }

    @Test
    void addNewItem() {
        long userId = 1;
        ItemControllerRequest itemControllerRequest = new ItemControllerRequest();
        ItemControllerResponse itemControllerResponse = new ItemControllerResponse();

        when(service.addItem(userId, itemControllerRequest)).thenReturn(itemControllerResponse);

        assertEquals(itemControllerResponse, controller.addNewItem(userId, itemControllerRequest));
    }

    @Test
    void addComment() {
        long userId = 1;
        long itemId = 1;
        CommentRequest commentRequest = new CommentRequest();
        CommentResponse commentResponse = new CommentResponse();

        when(service.addComment(userId, itemId, commentRequest)).thenReturn(commentResponse);

        assertEquals(commentResponse, controller.addComment(userId, itemId, commentRequest));
    }

    @Test
    void updateItem() {
        long userId = 1;
        long itemId = 1;
        ItemControllerRequest itemControllerRequest = new ItemControllerRequest();
        ItemControllerResponse itemControllerResponse = new ItemControllerResponse();

        when(service.updateItem(userId, itemId, itemControllerRequest)).thenReturn(itemControllerResponse);

        assertEquals(itemControllerResponse, controller.updateItem(userId, itemId, itemControllerRequest));
    }

    @Test
    void deleteItem() {
        long userId = 1;
        long itemId = 1;
        ItemControllerResponse itemControllerResponse = new ItemControllerResponse();

        when(service.deleteItem(userId, itemId)).thenReturn(itemControllerResponse);

        assertEquals(itemControllerResponse, controller.deleteItem(userId, itemId));

    }
}