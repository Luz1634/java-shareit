package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDb;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.model.GetNonExistObjectException;
import ru.practicum.shareit.exception.model.UnavailableObjectException;
import ru.practicum.shareit.item.dto.CommentRequest;
import ru.practicum.shareit.item.dto.ItemBookingInfoForOwner;
import ru.practicum.shareit.item.dto.ItemControllerResponse;
import ru.practicum.shareit.item.dto.ItemDb;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.dto.UserDb;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository repository;
    private final ItemMapper mapper;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    private final CommentMapper commentMapper;

    @Override
    public ItemControllerResponse getItem(long userId, long itemId) {
        UserDb userDb = userRepository.findById(userId)
                .orElseThrow(() -> new GetNonExistObjectException("User с заданным id = " + userId + " не найден"));
        ItemDb itemDb = repository.findById(itemId)
                .orElseThrow(() -> new GetNonExistObjectException("Item с заданным id = " + itemId + " не найден"));

        ItemControllerResponse item = mapper.toItemControllerResponse(mapper.toItem(itemDb));
        item.setComments(commentRepository.findByItemId(itemId).stream()
                .map(commentMapper::toComment)
                .map(commentMapper::toCommentResponse)
                .collect(toList()));

        if (userDb.getId() == itemDb.getOwner().getId()) {
            addLastAndNextBooking(item, userId);
        }

        return item;
    }

    @Override
    public List<ItemControllerResponse> getOwnerItems(long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new GetNonExistObjectException("User с заданным id = " + userId + " не найден"));

        List<ItemControllerResponse> items = repository.findByOwnerId(userId).stream()
                .map(mapper::toItem)
                .map(mapper::toItemControllerResponse)
                .collect(toList());

        for (ItemControllerResponse item : items) {
            addLastAndNextBooking(item, userId);
            item.setComments(commentRepository.findByItemId(item.getId()).stream()
                    .map(commentMapper::toComment)
                    .map(commentMapper::toCommentResponse)
                    .collect(toList()));
        }

        return items;
    }

    @Override
    public List<Item> searchItems(String text) {
        return repository.search(text).stream().map(mapper::toItem).collect(toList());
    }

    @Override
    public Item addItem(Item item, long userId) {
        User user = userMapper.toUser(userRepository.findById(userId)
                .orElseThrow(() -> new GetNonExistObjectException("User с заданным id = " + userId + " не найден")));
        item.setOwner(user);
        ItemDb itemDb = mapper.toItemDb(item);
        return mapper.toItem(repository.save(itemDb));
    }

    @Override
    public Comment addComment(long userId, long itemId, CommentRequest commentRequest) {
        User user = userMapper.toUser(userRepository.findById(userId)
                .orElseThrow(() -> new GetNonExistObjectException("User с заданным id = " + userId + " не найден")));
        ItemDb item = repository.findById(itemId)
                .orElseThrow(() -> new GetNonExistObjectException("Item с заданным id = " + itemId + " не найден"));
        if (item.getOwner().getId() == userId) {
            throw new UnavailableObjectException("User с заданным id = " + userId + " является владельцем");
        }
        bookingRepository.findFirstByBookerIdAndItemIdAndEndBefore(userId, itemId, LocalDateTime.now())
                .orElseThrow(() -> new UnavailableObjectException("User с заданным id = " + userId + " ещё не брал в аренду этот предмет"));

        Comment comment = commentMapper.toComment(commentRequest);
        comment.setAuthor(user);
        comment.setItem(mapper.toItem(item));
        comment.getItem().setOwner(userMapper.toUser(item.getOwner()));

        return commentMapper.toComment(commentRepository.save(commentMapper.toCommentDb(comment, LocalDateTime.now())));
    }

    @Override
    public Item updateItem(Item item, long userId) {
        ItemDb itemDb = repository.findById(item.getId())
                .orElseThrow(() -> new GetNonExistObjectException("Item с заданным id = " + item.getId() + " не найден"));
        Item itemOld = mapper.toItem(itemDb, itemDb.getOwner());

        if (userId != itemOld.getOwner().getId()) {
            throw new GetNonExistObjectException("указанный User: " + userId + " не является влалельцем Item: " + item.toString());
        }
        item.setOwner(itemOld.getOwner());

        if (item.getName() == null) {
            item.setName(itemOld.getName());
        }
        if (item.getDescription() == null) {
            item.setDescription(itemOld.getDescription());
        }
        if (item.getIsAvailable() == null) {
            item.setIsAvailable(itemOld.getIsAvailable());
        }

        return mapper.toItem(repository.save(mapper.toItemDb(item)));
    }

    @Override
    public Item deleteItem(long userId, long itemId) {
        ItemDb itemDb = repository.findById(itemId)
                .orElseThrow(() -> new GetNonExistObjectException("Item с заданным id = " + itemId + " не найден"));
        Item item = mapper.toItem(itemDb, itemDb.getOwner());

        if (userId != item.getOwner().getId()) {
            throw new GetNonExistObjectException("указанный User: " + userId + " не является влалельцем Item: " + item.toString());
        }

        repository.deleteById(itemId);
        return item;
    }

    private void addLastAndNextBooking(ItemControllerResponse item, long ownerId) {
        BookingDb lastBooking = bookingRepository
                .findFirstByItemIdAndItem_OwnerIdAndStartBeforeAndStatusOrderByStartDesc(
                        item.getId(),
                        ownerId,
                        LocalDateTime.now(),
                        BookingStatus.APPROVED);
        BookingDb nextBooking = bookingRepository
                .findFirstByItemIdAndItem_OwnerIdAndStartAfterAndStatusOrderByStartAsc(
                        item.getId(),
                        ownerId,
                        LocalDateTime.now(),
                        BookingStatus.APPROVED);

        if (lastBooking != null) {
            item.setLastBooking(new ItemBookingInfoForOwner(lastBooking.getId(), lastBooking.getBooker().getId()));
        }
        if (nextBooking != null) {
            item.setNextBooking(new ItemBookingInfoForOwner(nextBooking.getId(), nextBooking.getBooker().getId()));
        }
    }
}
