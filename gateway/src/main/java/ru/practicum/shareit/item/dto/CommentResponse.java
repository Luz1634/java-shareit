package ru.practicum.shareit.item.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class CommentResponse {
    private long id;
    private String authorName;
    private String text;
    private LocalDateTime created;
}
