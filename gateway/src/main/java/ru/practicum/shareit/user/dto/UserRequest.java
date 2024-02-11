package ru.practicum.shareit.user.dto;

import lombok.*;
import ru.practicum.shareit.validation.group.OnCreate;
import ru.practicum.shareit.validation.group.OnUpdate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class UserRequest {
    @NotBlank(message = "Name пустой или null", groups = OnCreate.class)
    private String name;
    @NotBlank(message = "Email пустой или null", groups = OnCreate.class)
    @Email(message = "Данный Email не подходит", groups = {OnCreate.class, OnUpdate.class})
    @Size(max = 255, message = "Email больше 255 символов", groups = {OnCreate.class, OnUpdate.class})
    private String email;
}
