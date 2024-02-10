package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
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
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @Mock
    private ItemRepository repository;
    @Mock
    private ItemMapper mapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private ItemRequestRepository itemRequestRepository;
    @Mock
    private CommentMapper commentMapper;
    @InjectMocks
    private ItemServiceImpl service;

    @Test
    void getItem_whenAllOk() {
        User owner = new User();
        owner.setId(1);

        Item item = new Item();
        item.setOwner(owner);

        User booker = new User();
        booker.setId(2);

        Booking lastBooking = new Booking(5, LocalDateTime.now(), LocalDateTime.now(), null, booker, BookingStatus.APPROVED);
        Booking nextBooking = new Booking(6, LocalDateTime.now(), LocalDateTime.now(), null, booker, BookingStatus.APPROVED);

        ItemControllerResponse itemControllerResponse = new ItemControllerResponse();
        itemControllerResponse.setLastBooking(new ItemBookingInfoForOwner(5, 2, lastBooking.getStart(), lastBooking.getEnd()));
        itemControllerResponse.setLastBooking(new ItemBookingInfoForOwner(6, 2, nextBooking.getStart(), nextBooking.getEnd()));

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(owner));
        when(repository.findById(anyLong())).thenReturn(Optional.of(item));
        when(mapper.toItemControllerResponse(item)).thenReturn(itemControllerResponse);
        when(commentRepository.findByItemId(anyLong())).thenReturn(List.of());
        when(bookingRepository.findFirstByItemIdAndItem_OwnerIdAndStartBeforeAndStatusOrderByStartDesc(anyLong(), anyLong(), any(LocalDateTime.class), any(BookingStatus.class)))
                .thenReturn(lastBooking);
        when(bookingRepository.findFirstByItemIdAndItem_OwnerIdAndStartAfterAndStatusOrderByStartAsc(anyLong(), anyLong(), any(LocalDateTime.class), any(BookingStatus.class)))
                .thenReturn(nextBooking);

        assertEquals(itemControllerResponse, service.getItem(1, 1));
    }

    @Test
    void getItem_whenUserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(GetNonExistObjectException.class, () -> service.getItem(1, 1));
    }

    @Test
    void getItem_whenItemNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(GetNonExistObjectException.class, () -> service.getItem(1, 1));
    }

    @Test
    void getOwnerItems_whenAllOk() {
        Item item = new Item();
        ItemControllerResponse itemControllerResponse = new ItemControllerResponse();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(repository.findByOwnerId(anyLong(), any(Pageable.class))).thenReturn(List.of(item, item, item));
        when(mapper.toItemControllerResponse(item)).thenReturn(itemControllerResponse);
        when(commentRepository.findByItemId(anyLong())).thenReturn(List.of());
        when(bookingRepository.findFirstByItemIdAndItem_OwnerIdAndStartBeforeAndStatusOrderByStartDesc(anyLong(), anyLong(), any(LocalDateTime.class), any(BookingStatus.class)))
                .thenReturn(null);
        when(bookingRepository.findFirstByItemIdAndItem_OwnerIdAndStartAfterAndStatusOrderByStartAsc(anyLong(), anyLong(), any(LocalDateTime.class), any(BookingStatus.class)))
                .thenReturn(null);

        List<ItemControllerResponse> itemControllerResponses = service.getOwnerItems(1, 1, 1);

        assertEquals(3, itemControllerResponses.size());
        assertEquals(itemControllerResponse, itemControllerResponses.get(0));
        assertEquals(itemControllerResponse, itemControllerResponses.get(1));
        assertEquals(itemControllerResponse, itemControllerResponses.get(2));
    }

    @Test
    void getOwnerItems_whenUserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(GetNonExistObjectException.class, () -> service.getOwnerItems(1, 1, 1));
    }

    @Test
    void searchItems_whenAllOk() {
        Item item = new Item();
        ItemControllerResponse itemControllerResponse = new ItemControllerResponse();

        when(repository.search(anyString(), any(Pageable.class))).thenReturn(List.of(item, item, item));
        when(mapper.toItemControllerResponse(item)).thenReturn(itemControllerResponse);

        List<ItemControllerResponse> itemControllerResponses = service.searchItems("text", 1, 1);

        assertEquals(3, itemControllerResponses.size());
        assertEquals(itemControllerResponse, itemControllerResponses.get(0));
        assertEquals(itemControllerResponse, itemControllerResponses.get(1));
        assertEquals(itemControllerResponse, itemControllerResponses.get(2));
    }

    @Test
    void addItem_whenAllOk() {
        User user = new User();

        ItemControllerRequest itemControllerRequest = new ItemControllerRequest();
        itemControllerRequest.setRequestId(1L);

        Item item = new Item();

        ItemControllerResponse itemControllerResponse = new ItemControllerResponse();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(mapper.toItem(itemControllerRequest)).thenReturn(item);
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.of(new ItemRequest()));
        when(repository.save(item)).thenReturn(item);
        when(mapper.toItemControllerResponse(item)).thenReturn(itemControllerResponse);

        assertEquals(itemControllerResponse, service.addItem(1, itemControllerRequest));
    }

    @Test
    void addItem_whenUserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(GetNonExistObjectException.class, () -> service.addItem(1, new ItemControllerRequest()));
    }

    @Test
    void addItem_whenItemRequestNotFound() {
        ItemControllerRequest itemControllerRequest = new ItemControllerRequest();
        itemControllerRequest.setRequestId(1L);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(mapper.toItem(itemControllerRequest)).thenReturn(new Item());
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(GetNonExistObjectException.class, () -> service.addItem(1, itemControllerRequest));
    }

    @Test
    void addComment_whenAllOk() {
        User owner = new User();
        Item item = new Item();
        owner.setId(1);
        item.setOwner(owner);

        CommentRequest commentRequest = new CommentRequest();
        Comment comment = new Comment();
        CommentResponse commentResponse = new CommentResponse();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(repository.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepository.findFirstByBookerIdAndItemIdAndEndBefore(anyLong(), anyLong(), any(LocalDateTime.class))).thenReturn(Optional.of(new Booking()));
        when(commentMapper.toComment(commentRequest)).thenReturn(comment);
        when(commentRepository.save(comment)).thenReturn(comment);
        when(commentMapper.toCommentResponse(comment)).thenReturn(commentResponse);

        assertEquals(commentResponse, service.addComment(2, 2, commentRequest));
    }

    @Test
    void addComment_whenUserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(GetNonExistObjectException.class, () -> service.addComment(1, 1, new CommentRequest()));
    }

    @Test
    void addComment_whenItemNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(GetNonExistObjectException.class, () -> service.addComment(1, 1, new CommentRequest()));
    }

    @Test
    void addComment_whenOwnerAddComment() {
        User owner = new User();
        Item item = new Item();
        owner.setId(1);
        item.setOwner(owner);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(repository.findById(anyLong())).thenReturn(Optional.of(item));

        assertThrows(UnavailableObjectException.class, () -> service.addComment(1, 1, new CommentRequest()));
    }

    @Test
    void addComment_whenBookerNotFound() {
        User owner = new User();
        Item item = new Item();
        owner.setId(1);
        item.setOwner(owner);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(repository.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepository.findFirstByBookerIdAndItemIdAndEndBefore(anyLong(), anyLong(), any(LocalDateTime.class))).thenReturn(Optional.empty());

        assertThrows(UnavailableObjectException.class, () -> service.addComment(2, 1, new CommentRequest()));
    }

    @Test
    void updateItem_WhenUpdateName() {
        ItemControllerRequest itemControllerRequest = new ItemControllerRequest();
        itemControllerRequest.setRequestId(1L);

        Item itemOld = new Item();
        itemOld.setDescription("description old");
        itemOld.setIsAvailable(true);

        Item itemUpdate = new Item();
        itemUpdate.setName("name");

        ItemControllerResponse itemControllerResponse = new ItemControllerResponse();

        User owner = new User();
        owner.setId(1);
        itemOld.setOwner(owner);

        when(repository.findById(anyLong())).thenReturn(Optional.of(itemOld));
        when(mapper.toItem(itemControllerRequest)).thenReturn(itemUpdate);
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.of(new ItemRequest()));
        when(repository.save(itemUpdate)).thenReturn(itemUpdate);
        when(mapper.toItemControllerResponse(itemUpdate)).thenReturn(itemControllerResponse);

        assertEquals(itemControllerResponse, service.updateItem(1, 1, itemControllerRequest));
    }

    @Test
    void updateItem_WhenItemNotFound() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(GetNonExistObjectException.class, () -> service.updateItem(1, 1, new ItemControllerRequest()));
    }

    @Test
    void updateItem_WhenUserNotOwner() {
        Item itemOld = new Item();
        User owner = new User();
        owner.setId(1);
        itemOld.setOwner(owner);

        when(repository.findById(anyLong())).thenReturn(Optional.of(itemOld));

        assertThrows(NonOwnerAccessException.class, () -> service.updateItem(2, 1, new ItemControllerRequest()));
    }

    @Test
    void updateItem_WhenRequestIdIsWrong() {
        ItemControllerRequest itemControllerRequest = new ItemControllerRequest();
        itemControllerRequest.setRequestId(1L);

        Item itemOld = new Item();
        User owner = new User();
        owner.setId(1);
        itemOld.setOwner(owner);
        Item itemUpdate = new Item();

        when(repository.findById(anyLong())).thenReturn(Optional.of(itemOld));
        when(mapper.toItem(itemControllerRequest)).thenReturn(itemUpdate);
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(GetNonExistObjectException.class, () -> service.updateItem(1, 1, itemControllerRequest));

    }

    @Test
    void deleteItem_whenAllOk() {
        Item item = new Item();
        User owner = new User();
        owner.setId(1);
        item.setOwner(owner);

        ItemControllerResponse itemControllerResponse = new ItemControllerResponse();

        when(repository.findById(anyLong())).thenReturn(Optional.of(item));
        when(mapper.toItemControllerResponse(item)).thenReturn(itemControllerResponse);

        assertEquals(itemControllerResponse, service.deleteItem(1, 1));
    }

    @Test
    void deleteItem_whenItemNotFound() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(GetNonExistObjectException.class, () -> service.deleteItem(1, 1));
    }

    @Test
    void deleteItem_whenUserNotOwner() {
        Item item = new Item();
        User owner = new User();
        owner.setId(1);
        item.setOwner(owner);

        when(repository.findById(anyLong())).thenReturn(Optional.of(item));

        assertThrows(NonOwnerAccessException.class, () -> service.deleteItem(2, 1));
    }
}