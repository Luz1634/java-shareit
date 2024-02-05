package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.model.GetNonExistObjectException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestRequest;
import ru.practicum.shareit.request.dto.ItemRequestResponse;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository repository;
    private final ItemRequestMapper mapper;

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Override
    public ItemRequestResponse getItemRequest(long userId, long requestId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GetNonExistObjectException("User с заданным id = " + userId + " не найден"));
        ItemRequest itemRequest = repository.findById(requestId)
                .orElseThrow(() -> new GetNonExistObjectException("ItemRequest с заданным id = " + requestId + " не найден"));

        return createItemRequestResponseWithItemsInfo(itemRequest);
    }

    @Override
    public List<ItemRequestResponse> getOwnerItemRequests(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GetNonExistObjectException("User с заданным id = " + userId + " не найден"));

        return repository.findByRequestorIdOrderByCreatedDesc(userId)
                .stream()
                .map(this::createItemRequestResponseWithItemsInfo)
                .collect(toList());
    }

    @Override
    public List<ItemRequestResponse> getItemRequests(long userId, int from, int size) {
        userRepository.findById(userId)
                .orElseThrow(() -> new GetNonExistObjectException("User с заданным id = " + userId + " не найден"));

        Pageable sortedByCreated = PageRequest.of(from / size, size, Sort.by("created").descending());

        return repository.findByRequestorIdNot(userId, sortedByCreated)
                .stream()
                .map(this::createItemRequestResponseWithItemsInfo)
                .collect(toList());
    }

    @Override
    public ItemRequestResponse addItemRequest(long userId, ItemRequestRequest itemRequestRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GetNonExistObjectException("User с заданным id = " + userId + " не найден"));

        ItemRequest itemRequest = mapper.toItemRequest(itemRequestRequest);
        itemRequest.setRequestor(user);
        itemRequest.setCreated(LocalDateTime.now());

        return mapper.toItemRequestResponse(repository.save(itemRequest));
    }

    private ItemRequestResponse createItemRequestResponseWithItemsInfo(ItemRequest itemRequest) {
        return mapper.toItemRequestResponse(itemRequest,
                itemRepository
                        .findByRequestIdOrderByRequestCreatedDesc(itemRequest.getId())
                        .stream()
                        .map((item) -> itemMapper.toItemInfoForItemRequest(item, itemRequest.getId()))
                        .collect(toList()));
    }
}
