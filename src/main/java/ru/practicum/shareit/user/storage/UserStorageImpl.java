package ru.practicum.shareit.user.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.model.AddDuplicateException;
import ru.practicum.shareit.exception.model.GetNonExistObjectException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class UserStorageImpl implements UserStorage {
    private final Map<Long, User> users;
    private long lastId;

    @Override
    public User getUser(long userId) {
        checkExistUser(userId);
        return users.get(userId);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User addUser(User user) {
        checkDuplicateEmail(user);
        user.setId(++lastId);
        user.setItems(new ArrayList<>());
        users.put(lastId, user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        checkExistUser(user.getId());

        User userOld = users.get(user.getId());

        if (user.getEmail() != null && !userOld.getEmail().equals(user.getEmail())) {
            checkDuplicateEmail(user);
            userOld.setEmail(user.getEmail());
        }

        if (user.getName() != null) {
            userOld.setName(user.getName());
        }

        return userOld;
    }

    @Override
    public User deleteUser(long userId) {
        checkExistUser(userId);
        User user = users.get(userId);
        users.remove(userId);
        return user;
    }

    private void checkExistUser(long userId) {
        if (!users.containsKey(userId)) {
            throw new GetNonExistObjectException("User с заданным id = " + userId + " не найден");
        }
    }

    private void checkDuplicateEmail(User user) {
        for (User existUser : users.values()) {
            if (existUser.getEmail().equals(user.getEmail())) {
                throw new AddDuplicateException("Это Email уже существует: " + user.getEmail());
            }
        }
    }
}
