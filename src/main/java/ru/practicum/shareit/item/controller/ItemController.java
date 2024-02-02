package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentRequest;
import ru.practicum.shareit.item.dto.CommentResponse;
import ru.practicum.shareit.item.dto.ItemControllerRequest;
import ru.practicum.shareit.item.dto.ItemControllerResponse;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/items")
@Slf4j
@RequiredArgsConstructor
@Validated
public class ItemController {

    private final ItemService service;

    @GetMapping("/{itemId}")
    public ItemControllerResponse getItem(@Min(value = 1, message = "UserId должно быть больше 0")
                                          @RequestHeader("X-Sharer-User-Id") long userId,
                                          @Min(value = 1, message = "ItemId должно быть больше 0")
                                          @PathVariable long itemId) {
        log.info("GET запрос - getItem, UserId: " + userId + ", ItemId: " + itemId);
        return service.getItem(userId, itemId);
    }

    @GetMapping
    public List<ItemControllerResponse> getOwnerItems(@Min(value = 1, message = "UserId должно быть больше 0")
                                                      @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("GET запрос - getOwnerItems, UserId: " + userId);
        return service.getOwnerItems(userId);
    }

    @GetMapping("/search")
    public List<ItemControllerResponse> searchItems(@Min(value = 1, message = "UserId должно быть больше 0")
                                                    @RequestHeader("X-Sharer-User-Id") long userId,
                                                    @RequestParam String text) {
        log.info("GET запрос - searchItems, UserId: " + userId + ", Text: " + text);
        if (text == null || text.isBlank()) {
            return new ArrayList<>();
        }
        return service.searchItems(text);
    }

    @PostMapping
    public ItemControllerResponse addNewItem(@Min(value = 1, message = "UserId должно быть больше 0")
                                             @RequestHeader("X-Sharer-User-Id") long userId,
                                             @Valid @RequestBody ItemControllerRequest itemControllerRequest) {
        log.info("POST запрос - addNewItem, UserId: " + userId +
                ", ItemControllerRequest: " + itemControllerRequest.toString());
        return service.addItem(userId, itemControllerRequest);
    }

    @PostMapping("/{itemId}/comment")
    public CommentResponse addComment(@Min(value = 1, message = "UserId должно быть больше 0")
                                      @RequestHeader("X-Sharer-User-Id") long userId,
                                      @Min(value = 1, message = "ItemId должно быть больше 0")
                                      @PathVariable long itemId,
                                      @Valid @RequestBody CommentRequest commentRequest) {
        return service.addComment(userId, itemId, commentRequest);
    }

    @PatchMapping("/{itemId}")
    public ItemControllerResponse updateItem(@Min(value = 1, message = "UserId должно быть больше 0")
                                             @RequestHeader("X-Sharer-User-Id") long userId,
                                             @Min(value = 1, message = "ItemId должно быть больше 0")
                                             @PathVariable long itemId,
                                             @RequestBody ItemControllerRequest itemControllerRequest) {
        log.info("PATCH запрос - updateItem, UserId: " + userId + ", ItemId: " + itemId +
                ", ItemControllerRequest: " + itemControllerRequest.toString());
        return service.updateItem(userId, itemId, itemControllerRequest);
    }

    @DeleteMapping("/{itemId}")
    public ItemControllerResponse deleteItem(@Min(value = 1, message = "UserId должно быть больше 0")
                                             @RequestHeader("X-Sharer-User-Id") long userId,
                                             @Min(value = 1, message = "ItemId должно быть больше 0")
                                             @PathVariable long itemId) {
        log.info("DELETE запрос - deleteItem, UserId: " + userId + ", ItemId: " + itemId);
        return service.deleteItem(userId, itemId);
    }
}
