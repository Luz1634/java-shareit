package ru.practicum.shareit.request.dto;

import lombok.*;
import ru.practicum.shareit.item.dto.ItemInfoForItemRequest;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class ItemRequestResponse {
    private long id;
    private String description;
    private LocalDateTime created;
    private List<ItemInfoForItemRequest> items;
}
