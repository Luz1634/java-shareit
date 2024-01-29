package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentRequest;
import ru.practicum.shareit.item.dto.CommentResponse;
import ru.practicum.shareit.item.dto.ItemControllerRequest;
import ru.practicum.shareit.item.dto.ItemControllerResponse;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/items")
@Slf4j
@RequiredArgsConstructor
@Validated
public class ItemController {

    private final ItemService service;
    private final ItemMapper mapper;
    private final CommentMapper commentMapper;

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
        return service.searchItems(text).stream()
                .map(mapper::toItemControllerResponse)
                .collect(toList());
    }

    @PostMapping
    public ItemControllerResponse addNewItem(@Valid @RequestBody ItemControllerRequest itemControllerRequest,
                                             @Min(value = 1, message = "UserId должно быть больше 0")
                                             @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("POST запрос - addNewItem, UserId: " + userId +
                ", ItemControllerRequest: " + itemControllerRequest.toString());
        return mapper.toItemControllerResponse(service.addItem(mapper.toItem(itemControllerRequest), userId));
    }

    @PostMapping("/{itemId}/comment")
    public CommentResponse addComment(@Min(value = 1, message = "UserId должно быть больше 0")
                                      @RequestHeader("X-Sharer-User-Id") long userId,
                                      @Min(value = 1, message = "ItemId должно быть больше 0")
                                      @PathVariable long itemId,
                                      @Valid @RequestBody CommentRequest commentRequest) {
        return commentMapper.toCommentResponse(service.addComment(userId, itemId, commentRequest));
    }

    @PatchMapping("/{itemId}")
    public ItemControllerResponse updateItem(@RequestBody ItemControllerRequest itemControllerRequest,
                                             @Min(value = 1, message = "UserId должно быть больше 0")
                                             @RequestHeader("X-Sharer-User-Id") long userId,
                                             @Min(value = 1, message = "ItemId должно быть больше 0")
                                             @PathVariable long itemId) {
        log.info("PATCH запрос - updateItem, UserId: " + userId + ", ItemId: " + itemId +
                ", ItemControllerRequest: " + itemControllerRequest.toString());
        Item item = mapper.toItem(itemControllerRequest);
        item.setId(itemId);
        return mapper.toItemControllerResponse(service.updateItem(item, userId));
    }

    @DeleteMapping("/{itemId}")
    public ItemControllerResponse deleteItem(@Min(value = 1, message = "UserId должно быть больше 0")
                                             @RequestHeader("X-Sharer-User-Id") long userId,
                                             @Min(value = 1, message = "ItemId должно быть больше 0")
                                             @PathVariable long itemId) {
        log.info("DELETE запрос - deleteItem, UserId: " + userId + ", ItemId: " + itemId);
        return mapper.toItemControllerResponse(service.deleteItem(userId, itemId));
    }
}
