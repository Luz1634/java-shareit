package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestRequest;
import ru.practicum.shareit.request.dto.ItemRequestResponse;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@Slf4j
@RequiredArgsConstructor
@Validated
public class ItemRequestController {

    private final ItemRequestService service;

    @GetMapping("/{requestId}")
    public ItemRequestResponse getItemRequest(@Min(value = 1, message = "UserId должно быть больше 0")
                                              @RequestHeader("X-Sharer-User-Id") long userId,
                                              @Min(value = 1, message = "RequestId должно быть больше 0")
                                              @PathVariable long requestId) {
        log.info("GET запрос - getItemRequest, UserId: " + userId + ", RequestId: " + requestId);
        return service.getItemRequest(userId, requestId);
    }

    @GetMapping
    public List<ItemRequestResponse> getOwnerItemRequests(@Min(value = 1, message = "UserId должно быть больше 0")
                                                          @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("GET запрос - getItemRequest, UserId: " + userId);
        return service.getOwnerItemRequests(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestResponse> getItemRequests(@Min(value = 1, message = "UserId должно быть больше 0")
                                                     @RequestHeader("X-Sharer-User-Id") long userId,
                                                     @Min(value = 0, message = "from должно быть больше или равно 0")
                                                     @RequestParam(defaultValue = "0") int from,
                                                     @Min(value = 1, message = "size должно быть больше 0")
                                                     @RequestParam(defaultValue = "20") int size) {
        log.info("GET запрос - getItemRequests, UserId: " + userId + ", from: " + from + ", size: " + size);
        return service.getItemRequests(userId, from, size);
    }

    @PostMapping
    public ItemRequestResponse addItemRequest(@Min(value = 1, message = "UserId должно быть больше 0")
                                              @RequestHeader("X-Sharer-User-Id") long userId,
                                              @Valid @RequestBody ItemRequestRequest itemRequest) {
        log.info("POST запрос - addItemRequest, UserId: " + userId + ", ItemRequestRequest: " + itemRequest.toString());
        return service.addItemRequest(userId, itemRequest);
    }
}
