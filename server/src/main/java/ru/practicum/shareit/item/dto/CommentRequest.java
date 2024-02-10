package ru.practicum.shareit.item.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class CommentRequest {
    @NotBlank(message = "Text пустой или null")
    @Size(max = 2048, message = "Text больше 2048 символов")
    private String text;
}
