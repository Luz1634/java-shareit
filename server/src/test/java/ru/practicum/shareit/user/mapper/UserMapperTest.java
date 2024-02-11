package ru.practicum.shareit.user.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dto.UserRequest;
import ru.practicum.shareit.user.dto.UserResponse;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class UserMapperTest {

    @InjectMocks
    private UserMapper mapper;

    @Test
    void toUser() {
        UserRequest userRequest = new UserRequest();
        userRequest.setName("name");
        userRequest.setEmail("email");

        User user = new User();
        user.setName("name");
        user.setEmail("email");

        assertEquals(user, mapper.toUser(userRequest));
    }

    @Test
    void toUserResponse() {
        User user = new User();
        user.setId(1);
        user.setName("name");
        user.setEmail("email");

        UserResponse userResponse = new UserResponse();
        userResponse.setId(1);
        userResponse.setName("name");
        userResponse.setEmail("email");

        assertEquals(userResponse, mapper.toUserResponse(user));
    }
}