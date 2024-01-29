package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.model.GetNonExistObjectException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final ItemRepository itemRepository;

    @Override
    public User getUser(long userId) {
        return mapper.toUser(repository.findById(userId)
                .orElseThrow(() -> new GetNonExistObjectException("User с заданным id = " + userId + " не найден")));
    }

    @Override
    public List<User> getAllUsers() {
        return repository.findAll().stream().map(mapper::toUser).collect(toList());
    }

    @Override
    @Transactional
    public User addUser(User user) {
        return mapper.toUser(repository.save(mapper.toUserDb(user)));
    }

    @Override
    public User updateUser(User user) {
        User userOld = mapper.toUser(repository.findById(user.getId())
                .orElseThrow(() -> new GetNonExistObjectException("User с заданным id = " + user.getId() + " не найден")));

        if (user.getName() == null) {
            user.setName(userOld.getName());
        }
        if (user.getEmail() == null) {
            user.setEmail(userOld.getEmail());
        }

        return mapper.toUser(repository.save(mapper.toUserDb(user)));
    }

    @Override
    @Transactional
    public User deleteUser(long userId) {
        User user = mapper.toUser(repository.findById(userId)
                .orElseThrow(() -> new GetNonExistObjectException("User с заданным id = " + userId + " не найден")));
        itemRepository.deleteByOwnerId(userId);
        repository.deleteById(userId);
        return user;
    }
}
