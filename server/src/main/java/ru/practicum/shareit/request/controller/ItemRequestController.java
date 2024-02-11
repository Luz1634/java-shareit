package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestRequest;
import ru.practicum.shareit.request.dto.ItemRequestResponse;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@Slf4j
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestService service;

    @GetMapping("/{requestId}")
    public ItemRequestResponse getItemRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                              @PathVariable long requestId) {
        log.info("GET запрос - getItemRequest, UserId: " + userId + ", RequestId: " + requestId);
        return service.getItemRequest(userId, requestId);
    }

    @GetMapping
    public List<ItemRequestResponse> getOwnerItemRequests(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("GET запрос - getItemRequest, UserId: " + userId);
        return service.getOwnerItemRequests(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestResponse> getItemRequests(@RequestHeader("X-Sharer-User-Id") long userId,
                                                     @RequestParam(defaultValue = "0") int from,
                                                     @RequestParam(defaultValue = "20") int size) {
        log.info("GET запрос - getItemRequests, UserId: " + userId + ", from: " + from + ", size: " + size);
        return service.getItemRequests(userId, from, size);
    }

    @PostMapping
    public ItemRequestResponse addItemRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                              @RequestBody ItemRequestRequest itemRequest) {
        log.info("POST запрос - addItemRequest, UserId: " + userId + ", ItemRequestRequest: " + itemRequest.toString());
        return service.addItemRequest(userId, itemRequest);
    }
}
