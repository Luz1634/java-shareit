package ru.practicum.shareit.user.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class UserRequestUpdate {
    private String name;
    @Email(message = "Данный Email не подходит")
    @Size(max = 255, message = "Email больше 255 символов")
    private String email;
}
