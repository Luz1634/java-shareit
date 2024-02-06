package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    private ItemRepository repository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void findFirstByOwnerId() {
    }

    @Test
    void findByOwnerId() {
    }

    @Test
    void findByRequestIdOrderByRequestCreatedDesc() {
    }

    @Test
    void search() {
        User user = new User(1, "name", "user@email.com");
        userRepository.save(user);
        Item item1 = new Item(1, "item name1", "description 1", true, user, null, null);
        repository.save(item1);
        repository.save(new Item(2, "item name2", "description 2", true, user, null, null));

        List<Item> items = repository.search("name1", PageRequest.of(0, 5));

        assertEquals(1, items.size());
        assertEquals(item1, items.get(0));
    }

    @Test
    void deleteByOwnerId() {
    }
}