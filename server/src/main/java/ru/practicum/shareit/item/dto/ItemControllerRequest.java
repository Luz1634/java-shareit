package ru.practicum.shareit.item.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class ItemControllerRequest {
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;
}
