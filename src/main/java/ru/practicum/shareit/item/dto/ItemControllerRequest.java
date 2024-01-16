package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class ItemControllerRequest {
    @NotBlank(message = "Name пустой или null")
    private String name;
    @NotBlank(message = "Description пустой или null")
    private String description;
    @NotNull(message = "Available пустой или null")
    private Boolean available;
}
