package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentRequest;
import ru.practicum.shareit.item.dto.CommentResponse;
import ru.practicum.shareit.item.dto.ItemControllerRequest;
import ru.practicum.shareit.item.dto.ItemControllerResponse;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RestController
@RequestMapping("/items")
@Slf4j
@RequiredArgsConstructor
public class ItemController {

    private final ItemService service;

    @GetMapping("/{itemId}")
    public ItemControllerResponse getItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                          @PathVariable long itemId) {
        log.info("GET запрос - getItem, UserId: " + userId + ", ItemId: " + itemId);
        return service.getItem(userId, itemId);
    }

    @GetMapping
    public List<ItemControllerResponse> getOwnerItems(@RequestHeader("X-Sharer-User-Id") long userId,
                                                      @RequestParam(defaultValue = "0") int from,
                                                      @RequestParam(defaultValue = "20") int size) {
        log.info("GET запрос - getOwnerItems, UserId: " + userId + ", from: " + from + ", size: " + size);
        return service.getOwnerItems(userId, from, size);
    }

    @GetMapping("/search")
    public List<ItemControllerResponse> searchItems(@RequestHeader("X-Sharer-User-Id") long userId,
                                                    @RequestParam String text,
                                                    @RequestParam(defaultValue = "0") int from,
                                                    @RequestParam(defaultValue = "20") int size) {
        log.info("GET запрос - searchItems, UserId: " + userId + ", Text: " + text + ", from: " + from + ", size: " + size);
        return service.searchItems(text, from, size);
    }

    @PostMapping
    public ItemControllerResponse addNewItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @RequestBody ItemControllerRequest itemControllerRequest) {
        log.info("POST запрос - addNewItem, UserId: " + userId +
                ", ItemControllerRequest: " + itemControllerRequest.toString());
        return service.addItem(userId, itemControllerRequest);
    }

    @PostMapping("/{itemId}/comment")
    public CommentResponse addComment(@RequestHeader("X-Sharer-User-Id") long userId,
                                      @PathVariable long itemId,
                                      @RequestBody CommentRequest commentRequest) {
        return service.addComment(userId, itemId, commentRequest);
    }

    @PatchMapping("/{itemId}")
    public ItemControllerResponse updateItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable long itemId,
                                             @RequestBody ItemControllerRequest itemControllerRequest) {
        log.info("PATCH запрос - updateItem, UserId: " + userId + ", ItemId: " + itemId +
                ", ItemControllerRequest: " + itemControllerRequest.toString());
        return service.updateItem(userId, itemId, itemControllerRequest);
    }

    @DeleteMapping("/{itemId}")
    public ItemControllerResponse deleteItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable long itemId) {
        log.info("DELETE запрос - deleteItem, UserId: " + userId + ", ItemId: " + itemId);
        return service.deleteItem(userId, itemId);
    }
}
