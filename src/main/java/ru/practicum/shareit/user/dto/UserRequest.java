package ru.practicum.shareit.user.dto;

import lombok.*;

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
    @NotBlank(message = "Name пустой или null")
    private String name;
    @NotBlank(message = "Email пустой или null")
    @Email(message = "Данный Email не подходит")
    @Size(max = 255, message = "Email больше 255 символов")
    private String email;
}
