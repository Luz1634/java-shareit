package ru.practicum.shareit.item.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class ItemControllerResponse {
    private long id;
    private String name;
    private String description;
    private boolean available;
}
