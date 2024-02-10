package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.model.GetNonExistObjectException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.dto.UserRequest;
import ru.practicum.shareit.user.dto.UserResponse;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final UserMapper mapper;

    private final ItemRepository itemRepository;

    @Override
    public UserResponse getUser(long userId) {
        return mapper.toUserResponse(repository.findById(userId)
                .orElseThrow(() -> new GetNonExistObjectException("User с заданным id = " + userId + " не найден")));
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return repository.findAll().stream().map(mapper::toUserResponse).collect(toList());
    }

    @Override
    @Transactional
    public UserResponse addUser(UserRequest userRequest) {
        return mapper.toUserResponse(repository.save(mapper.toUser(userRequest)));
    }

    @Override
    @Transactional
    public UserResponse updateUser(long userId, UserRequest userRequest) {
        User userOld = repository.findById(userId)
                .orElseThrow(() -> new GetNonExistObjectException("User с заданным id = " + userId + " не найден"));

        User user = mapper.toUser(userRequest);
        user.setId(userId);

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(userOld.getName());
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            user.setEmail(userOld.getEmail());
        }

        return mapper.toUserResponse(repository.save(user));
    }

    @Override
    @Transactional
    public UserResponse deleteUser(long userId) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new GetNonExistObjectException("User с заданным id = " + userId + " не найден"));
        itemRepository.deleteByOwnerId(userId);
        repository.deleteById(userId);
        return mapper.toUserResponse(user);
    }
}
