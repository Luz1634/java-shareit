package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.model.GetNonExistObjectException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.dto.UserRequest;
import ru.practicum.shareit.user.dto.UserResponse;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository repository;
    @Mock
    private UserMapper mapper;
    @Mock
    private ItemRepository itemRepository;
    @InjectMocks
    private UserServiceImpl service;

    @Test
    void getUser_whenUserExist() {
        User user = new User();
        UserResponse userResponse = new UserResponse();

        when(repository.findById(anyLong())).thenReturn(Optional.of(user));
        when(mapper.toUserResponse(user)).thenReturn(userResponse);

        assertEquals(userResponse, service.getUser(1));
    }

    @Test
    void getUser_whenUserNotFound() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(GetNonExistObjectException.class, () -> service.getUser(1));
    }

    @Test
    void getAllUsers_whenUsersExist() {
        User user = new User();
        UserResponse userResponse = new UserResponse();

        when(repository.findAll()).thenReturn(List.of(user, user, user));
        when(mapper.toUserResponse(user)).thenReturn(userResponse);

        List<UserResponse> usersResponse = service.getAllUsers();

        assertEquals(3, usersResponse.size());
        assertEquals(userResponse, usersResponse.get(0));
        assertEquals(userResponse, usersResponse.get(1));
        assertEquals(userResponse, usersResponse.get(2));
    }

    @Test
    void getAllUsers_whenUsersNotFound() {
        when(repository.findAll()).thenReturn(List.of());

        assertEquals(0, service.getAllUsers().size());
    }

    @Test
    void addUser() {
        UserRequest userRequest = new UserRequest();
        User user = new User();
        UserResponse userResponse = new UserResponse();

        when(mapper.toUser(userRequest)).thenReturn(user);
        when(repository.save(user)).thenReturn(user);
        when(mapper.toUserResponse(user)).thenReturn(userResponse);

        assertEquals(userResponse, service.addUser(userRequest));
    }

    @Test
    void updateUser_whenUserNotFound() {
        UserRequest userRequest = new UserRequest();

        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(GetNonExistObjectException.class, () -> service.updateUser(1, userRequest));
    }

    @Test
    void updateUser_whenUserUpdateName() {
        UserRequest userRequest = new UserRequest();
        userRequest.setName("name");

        User userOld = new User();
        userOld.setName("name old");
        userOld.setEmail("email old");

        User user = new User();
        user.setName("name");

        UserResponse userResponse = new UserResponse();
        userResponse.setName("name");
        userResponse.setEmail("email old");

        when(repository.findById(anyLong())).thenReturn(Optional.of(user));
        when(mapper.toUser(userRequest)).thenReturn(user);
        when(repository.save(user)).thenReturn(user);
        when(mapper.toUserResponse(user)).thenReturn(userResponse);

        assertEquals(userResponse, service.updateUser(1, userRequest));
    }

    @Test
    void updateUser_whenUserUpdateEmail() {
        UserRequest userRequest = new UserRequest();
        userRequest.setEmail("email");

        User userOld = new User();
        userOld.setName("name old");
        userOld.setEmail("email old");

        User user = new User();
        user.setEmail("email");

        UserResponse userResponse = new UserResponse();
        userResponse.setName("name old");
        userResponse.setEmail("email");

        when(repository.findById(anyLong())).thenReturn(Optional.of(user));
        when(mapper.toUser(userRequest)).thenReturn(user);
        when(repository.save(user)).thenReturn(user);
        when(mapper.toUserResponse(user)).thenReturn(userResponse);

        assertEquals(userResponse, service.updateUser(1, userRequest));
    }

    @Test
    void deleteUser_whenUserNotFound() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(GetNonExistObjectException.class, () -> service.deleteUser(1));
    }

    @Test
    void deleteUser_whenUserExist() {
        User user = new User();
        UserResponse userResponse = new UserResponse();
        when(repository.findById(anyLong())).thenReturn(Optional.of(user));
        when(mapper.toUserResponse(user)).thenReturn(userResponse);

        assertEquals(userResponse, service.deleteUser(1));
    }
}