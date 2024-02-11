package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.item.dto.CommentRequest;
import ru.practicum.shareit.item.dto.CommentResponse;
import ru.practicum.shareit.item.dto.ItemControllerRequest;
import ru.practicum.shareit.item.dto.ItemControllerResponse;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
class ItemControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ItemService service;

    @Test
    @SneakyThrows
    void getItem_whenAllOk() {
        long userId = 1;
        long itemId = 1;

        ItemControllerResponse itemControllerResponse = new ItemControllerResponse();

        when(service.getItem(userId, itemId)).thenReturn(itemControllerResponse);

        String response = mockMvc.perform(MockMvcRequestBuilders.get("/items/{itemId}", itemId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(response, objectMapper.writeValueAsString(itemControllerResponse));
    }

    @Test
    @SneakyThrows
    void getOwnerItems_whenAllOk() {
        long userId = 1;
        int from = 0;
        int size = 1;

        List<ItemControllerResponse> itemControllerResponse = List.of();

        when(service.getOwnerItems(userId, from, size)).thenReturn(itemControllerResponse);

        String response = mockMvc.perform(MockMvcRequestBuilders.get("/items")
                        .header("X-Sharer-User-Id", userId)
                        .param("from", from + "")
                        .param("size", size + ""))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(response, objectMapper.writeValueAsString(itemControllerResponse));
    }

    @Test
    @SneakyThrows
    void searchItems_whenAllOk() {
        long userId = 1;
        String text = "text";
        int from = 0;
        int size = 1;

        List<ItemControllerResponse> itemControllerResponse = List.of();

        when(service.searchItems(text, from, size)).thenReturn(itemControllerResponse);

        String response = mockMvc.perform(MockMvcRequestBuilders.get("/items/search")
                        .header("X-Sharer-User-Id", userId)
                        .param("text", text)
                        .param("from", from + "")
                        .param("size", size + ""))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(response, objectMapper.writeValueAsString(itemControllerResponse));
    }

    @Test
    @SneakyThrows
    void addNewItem_whenAllOk() {
        long userId = 1;
        ItemControllerRequest itemControllerRequest = new ItemControllerRequest();
        itemControllerRequest.setName("name");
        itemControllerRequest.setDescription("description");
        itemControllerRequest.setAvailable(true);

        ItemControllerResponse itemControllerResponse = new ItemControllerResponse();

        when(service.addItem(userId, itemControllerRequest)).thenReturn(itemControllerResponse);

        String response = mockMvc.perform(MockMvcRequestBuilders.post("/items")
                        .header("X-Sharer-User-Id", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemControllerRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(response, objectMapper.writeValueAsString(itemControllerResponse));
    }

    @Test
    @SneakyThrows
    void addComment_whenAllOk() {
        long userId = 1;
        long itemId = 1;
        CommentRequest commentRequest = new CommentRequest();
        commentRequest.setText("text");

        CommentResponse commentResponse = new CommentResponse();

        when(service.addComment(userId, itemId, commentRequest)).thenReturn(commentResponse);

        String response = mockMvc.perform(MockMvcRequestBuilders.post("/items/{itemId}/comment", itemId)
                        .header("X-Sharer-User-Id", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(commentRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(response, objectMapper.writeValueAsString(commentResponse));
    }

    @Test
    @SneakyThrows
    void updateItem_whenAllOk() {
        long userId = 1;
        long itemId = 1;
        ItemControllerRequest itemControllerRequest = new ItemControllerRequest();
        itemControllerRequest.setName("name");
        itemControllerRequest.setDescription("description");
        itemControllerRequest.setAvailable(true);

        ItemControllerResponse itemControllerResponse = new ItemControllerResponse();

        when(service.updateItem(userId, itemId, itemControllerRequest)).thenReturn(itemControllerResponse);

        String response = mockMvc.perform(MockMvcRequestBuilders.patch("/items/{itemId}", itemId)
                        .header("X-Sharer-User-Id", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemControllerRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(response, objectMapper.writeValueAsString(itemControllerResponse));
    }

    @Test
    @SneakyThrows
    void deleteItem_whenAllOk() {
        long userId = 1;
        long itemId = 1;

        ItemControllerResponse itemControllerResponse = new ItemControllerResponse();

        when(service.deleteItem(userId, itemId)).thenReturn(itemControllerResponse);

        String response = mockMvc.perform(MockMvcRequestBuilders.delete("/items/{itemId}", itemId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(response, objectMapper.writeValueAsString(itemControllerResponse));
    }

}