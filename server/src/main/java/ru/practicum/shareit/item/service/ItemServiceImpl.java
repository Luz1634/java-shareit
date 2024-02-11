package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.model.GetNonExistObjectException;
import ru.practicum.shareit.exception.model.NonOwnerAccessException;
import ru.practicum.shareit.exception.model.UnavailableObjectException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {

    private final ItemRepository repository;
    private final ItemMapper mapper;

    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemRequestRepository itemRequestRepository;
    private final CommentMapper commentMapper;

    @Override
    public ItemControllerResponse getItem(long userId, long itemId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GetNonExistObjectException("User с заданным id = " + userId + " не найден"));
        Item item = repository.findById(itemId)
                .orElseThrow(() -> new GetNonExistObjectException("Item с заданным id = " + itemId + " не найден"));

        ItemControllerResponse itemResponse = mapper.toItemControllerResponse(item);

        itemResponse.setComments(getItemCommentsToResponse(itemId));

        if (user.getId() == item.getOwner().getId()) {
            addLastAndNextBooking(userId, itemResponse);
        }

        return itemResponse;
    }

    @Override
    public List<ItemControllerResponse> getOwnerItems(long userId, int from, int size) {
        userRepository.findById(userId)
                .orElseThrow(() -> new GetNonExistObjectException("User с заданным id = " + userId + " не найден"));

        Pageable unsortedPageable = PageRequest.of(from / size, size);

        List<ItemControllerResponse> itemsResponse = repository.findByOwnerId(userId, unsortedPageable)
                .stream()
                .map(mapper::toItemControllerResponse)
                .collect(toList());

        for (ItemControllerResponse item : itemsResponse) {
            item.setComments(getItemCommentsToResponse(item.getId()));
            addLastAndNextBooking(userId, item);
        }

        return itemsResponse;
    }

    @Override
    public List<ItemControllerResponse> searchItems(String text, int from, int size) {
        Pageable unsortedPageable = PageRequest.of(from / size, size);

        return repository.search(text, unsortedPageable)
                .stream()
                .map(mapper::toItemControllerResponse)
                .collect(toList());
    }

    @Override
    @Transactional
    public ItemControllerResponse addItem(long userId, ItemControllerRequest itemRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GetNonExistObjectException("User с заданным id = " + userId + " не найден"));

        Item item = mapper.toItem(itemRequest);
        item.setOwner(user);

        if (itemRequest.getRequestId() != null) {
            item.setRequest(itemRequestRepository.findById(itemRequest.getRequestId())
                    .orElseThrow(() -> new GetNonExistObjectException("ItemRequest с заданным id = " + itemRequest.getRequestId() + " не найден")));
        }

        return mapper.toItemControllerResponse(repository.save(item));
    }

    @Override
    @Transactional
    public CommentResponse addComment(long userId, long itemId, CommentRequest commentRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GetNonExistObjectException("User с заданным id = " + userId + " не найден"));
        Item item = repository.findById(itemId)
                .orElseThrow(() -> new GetNonExistObjectException("Item с заданным id = " + itemId + " не найден"));
        if (item.getOwner().getId() == userId) {
            throw new UnavailableObjectException("User с заданным id = " + userId + " является владельцем");
        }
        bookingRepository.findFirstByBookerIdAndItemIdAndEndBefore(userId, itemId, LocalDateTime.now())
                .orElseThrow(() -> new UnavailableObjectException("User с заданным id = " + userId + " ещё не брал в аренду этот предмет"));

        Comment comment = commentMapper.toComment(commentRequest);
        comment.setAuthor(user);
        comment.setItem(item);
        comment.setCreated(LocalDateTime.now());

        return commentMapper.toCommentResponse(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public ItemControllerResponse updateItem(long userId, long itemId, ItemControllerRequest itemRequest) {
        Item itemOld = repository.findById(itemId)
                .orElseThrow(() -> new GetNonExistObjectException("Item с заданным id = " + itemId + " не найден"));
        if (userId != itemOld.getOwner().getId()) {
            throw new NonOwnerAccessException("указанный User с id = " + userId + " не является влалельцем Item c id = " + itemId);
        }

        Item itemUpdate = mapper.toItem(itemRequest);

        if (itemUpdate.getName() == null || itemUpdate.getName().isBlank()) {
            itemUpdate.setName(itemOld.getName());
        }
        if (itemUpdate.getDescription() == null || itemUpdate.getDescription().isBlank()) {
            itemUpdate.setDescription(itemOld.getDescription());
        }
        if (itemUpdate.getIsAvailable() == null) {
            itemUpdate.setIsAvailable(itemOld.getIsAvailable());
        }

        itemUpdate.setId(itemId);
        itemUpdate.setOwner(itemOld.getOwner());

        if (itemRequest.getRequestId() != null) {
            itemUpdate.setRequest(itemRequestRepository.findById(itemRequest.getRequestId())
                    .orElseThrow(() -> new GetNonExistObjectException("ItemRequest с заданным id = " + itemRequest.getRequestId() + " не найден")));
        }

        return mapper.toItemControllerResponse(repository.save(itemUpdate));
    }

    @Override
    @Transactional
    public ItemControllerResponse deleteItem(long userId, long itemId) {
        Item item = repository.findById(itemId)
                .orElseThrow(() -> new GetNonExistObjectException("Item с заданным id = " + itemId + " не найден"));
        if (userId != item.getOwner().getId()) {
            throw new NonOwnerAccessException("указанный User с id = " + userId + " не является влалельцем Item c id = " + itemId);
        }

        repository.deleteById(itemId);
        return mapper.toItemControllerResponse(item);
    }

    private void addLastAndNextBooking(long ownerId, ItemControllerResponse item) {
        Booking lastBooking = bookingRepository
                .findFirstByItemIdAndItem_OwnerIdAndStartBeforeAndStatusOrderByStartDesc(
                        item.getId(),
                        ownerId,
                        LocalDateTime.now(),
                        BookingStatus.APPROVED);
        Booking nextBooking = bookingRepository
                .findFirstByItemIdAndItem_OwnerIdAndStartAfterAndStatusOrderByStartAsc(
                        item.getId(),
                        ownerId,
                        LocalDateTime.now(),
                        BookingStatus.APPROVED);

        if (lastBooking != null) {
            item.setLastBooking(new ItemBookingInfoForOwner(
                    lastBooking.getId(),
                    lastBooking.getBooker().getId(),
                    lastBooking.getStart(),
                    lastBooking.getEnd()));
        }
        if (nextBooking != null) {
            item.setNextBooking(new ItemBookingInfoForOwner(
                    nextBooking.getId(),
                    nextBooking.getBooker().getId(),
                    nextBooking.getStart(),
                    nextBooking.getEnd()));
        }
    }

    private List<CommentResponse> getItemCommentsToResponse(long itemId) {
        return commentRepository.findByItemId(itemId)
                .stream()
                .map(commentMapper::toCommentResponse)
                .collect(toList());
    }
}
