package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {
    @Mock
    private ItemRequestRepository repository;
    @Mock
    private ItemRequestMapper mapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private ItemMapper itemMapper;
    @InjectMocks
    private ItemRequestServiceImpl service;

    @Test
    void getItemRequest_whenAllOk() {
        User user = new User();
        ItemRequest itemRequest = new ItemRequest();
        ItemRequestResponse itemRequestResponse = new ItemRequestResponse();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(repository.findById(anyLong())).thenReturn(Optional.of(itemRequest));
        when(itemRepository.findByRequestIdOrderByRequestCreatedDesc(anyLong())).thenReturn(List.of());
        when(mapper.toItemRequestResponse(itemRequest, List.of())).thenReturn(itemRequestResponse);

        assertEquals(itemRequestResponse, service.getItemRequest(1, 1));
    }

    @Test
    void getItemRequest_whenUserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(GetNonExistObjectException.class, () -> service.getItemRequest(1, 1));
    }

    @Test
    void getItemRequest_whenItemRequestNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(GetNonExistObjectException.class, () -> service.getItemRequest(1, 1));
    }

    @Test
    void getOwnerItemRequests_whenAllOk() {
        User user = new User();
        ItemRequest itemRequest = new ItemRequest();
        ItemRequestResponse itemRequestResponse = new ItemRequestResponse();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(repository.findByRequestorIdOrderByCreatedDesc(anyLong())).thenReturn(List.of(itemRequest, itemRequest, itemRequest));
        when(itemRepository.findByRequestIdOrderByRequestCreatedDesc(anyLong())).thenReturn(List.of());
        when(mapper.toItemRequestResponse(itemRequest, List.of())).thenReturn(itemRequestResponse);

        List<ItemRequestResponse> itemsRequestResponse = service.getOwnerItemRequests(1);

        assertEquals(3, itemsRequestResponse.size());
        assertEquals(itemRequestResponse, itemsRequestResponse.get(0));
        assertEquals(itemRequestResponse, itemsRequestResponse.get(1));
        assertEquals(itemRequestResponse, itemsRequestResponse.get(2));
    }

    @Test
    void getOwnerItemRequests_whenUserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(GetNonExistObjectException.class, () -> service.getItemRequest(1, 1));
    }

    @Test
    void getItemRequests_whenAllOk() {
        User user = new User();
        ItemRequest itemRequest = new ItemRequest();
        ItemRequestResponse itemRequestResponse = new ItemRequestResponse();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(repository.findByRequestorIdNot(anyLong(), any(Pageable.class))).thenReturn(List.of(itemRequest, itemRequest, itemRequest));
        when(itemRepository.findByRequestIdOrderByRequestCreatedDesc(anyLong())).thenReturn(List.of());
        when(mapper.toItemRequestResponse(itemRequest, List.of())).thenReturn(itemRequestResponse);

        List<ItemRequestResponse> itemsRequestResponse = service.getItemRequests(1, 1, 1);

        assertEquals(3, itemsRequestResponse.size());
        assertEquals(itemRequestResponse, itemsRequestResponse.get(0));
        assertEquals(itemRequestResponse, itemsRequestResponse.get(1));
        assertEquals(itemRequestResponse, itemsRequestResponse.get(2));
    }

    @Test
    void getItemRequests_whenUserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(GetNonExistObjectException.class, () -> service.getItemRequests(1, 1, 1));
    }

    @Test
    void addItemRequest_whenAllOk() {
        User user = new User();
        ItemRequestRequest itemRequestRequest = new ItemRequestRequest();
        ItemRequest itemRequest = new ItemRequest();
        ItemRequestResponse itemRequestResponse = new ItemRequestResponse();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(mapper.toItemRequest(itemRequestRequest)).thenReturn(itemRequest);
        when(repository.save(itemRequest)).thenReturn(itemRequest);
        when(mapper.toItemRequestResponse(itemRequest)).thenReturn(itemRequestResponse);

        assertEquals(itemRequestResponse, service.addItemRequest(1, itemRequestRequest));
    }

    @Test
    void addItemRequest_whenUserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(GetNonExistObjectException.class, () -> service.addItemRequest(1, new ItemRequestRequest()));
    }
}