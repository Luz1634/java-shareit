package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Item {
    private long id;
    private String name;
    private String description;
    private Boolean isAvailable;
    private User owner;
    private ItemRequest request;
    private List<Comment> comments;
}
