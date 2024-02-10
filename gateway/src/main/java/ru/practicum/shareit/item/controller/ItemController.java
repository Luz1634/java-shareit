package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.client.ItemClient;
import ru.practicum.shareit.item.dto.CommentRequest;
import ru.practicum.shareit.item.dto.ItemControllerRequest;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.ArrayList;

@RestController
@RequestMapping("/items")
@Slf4j
@RequiredArgsConstructor
@Validated
public class ItemController {

    private final ItemClient client;

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@Min(value = 1, message = "UserId должно быть больше 0")
                                          @RequestHeader("X-Sharer-User-Id") long userId,
                                          @Min(value = 1, message = "ItemId должно быть больше 0")
                                          @PathVariable long itemId) {
        log.info("GET запрос - getItem, UserId: " + userId + ", ItemId: " + itemId);
        return client.getItem(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getOwnerItems(@Min(value = 1, message = "UserId должно быть больше 0")
                                                @RequestHeader("X-Sharer-User-Id") long userId,
                                                @Min(value = 0, message = "from должно быть больше или равно 0")
                                                @RequestParam(defaultValue = "0") int from,
                                                @Min(value = 1, message = "size должно быть больше 0")
                                                @RequestParam(defaultValue = "20") int size) {
        log.info("GET запрос - getOwnerItems, UserId: " + userId + ", from: " + from + ", size: " + size);
        return client.getOwnerItems(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@Min(value = 1, message = "UserId должно быть больше 0")
                                              @RequestHeader("X-Sharer-User-Id") long userId,
                                              @RequestParam String text,
                                              @Min(value = 0, message = "from должно быть больше или равно 0")
                                              @RequestParam(defaultValue = "0") int from,
                                              @Min(value = 1, message = "size должно быть больше 0")
                                              @RequestParam(defaultValue = "20") int size) {
        log.info("GET запрос - searchItems, UserId: " + userId + ", Text: " + text + ", from: " + from + ", size: " + size);
        if (text == null || text.isBlank()) {
            return ResponseEntity.ok(new ArrayList<>());
        }
        return client.searchItems(userId, text, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> addNewItem(@Min(value = 1, message = "UserId должно быть больше 0")
                                             @RequestHeader("X-Sharer-User-Id") long userId,
                                             @Valid @RequestBody ItemControllerRequest itemControllerRequest) {
        log.info("POST запрос - addNewItem, UserId: " + userId +
                ", ItemControllerRequest: " + itemControllerRequest.toString());
        return client.addItem(userId, itemControllerRequest);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@Min(value = 1, message = "UserId должно быть больше 0")
                                             @RequestHeader("X-Sharer-User-Id") long userId,
                                             @Min(value = 1, message = "ItemId должно быть больше 0")
                                             @PathVariable long itemId,
                                             @Valid @RequestBody CommentRequest commentRequest) {
        return client.addComment(userId, itemId, commentRequest);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@Min(value = 1, message = "UserId должно быть больше 0")
                                             @RequestHeader("X-Sharer-User-Id") long userId,
                                             @Min(value = 1, message = "ItemId должно быть больше 0")
                                             @PathVariable long itemId,
                                             @RequestBody ItemControllerRequest itemControllerRequest) {
        log.info("PATCH запрос - updateItem, UserId: " + userId + ", ItemId: " + itemId +
                ", ItemControllerRequest: " + itemControllerRequest.toString());
        return client.updateItem(userId, itemId, itemControllerRequest);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Object> deleteItem(@Min(value = 1, message = "UserId должно быть больше 0")
                                             @RequestHeader("X-Sharer-User-Id") long userId,
                                             @Min(value = 1, message = "ItemId должно быть больше 0")
                                             @PathVariable long itemId) {
        log.info("DELETE запрос - deleteItem, UserId: " + userId + ", ItemId: " + itemId);
        return client.deleteItem(userId, itemId);
    }
}
