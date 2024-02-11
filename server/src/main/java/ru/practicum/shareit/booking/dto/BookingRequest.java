package ru.practicum.shareit.booking.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class BookingRequest {
    private LocalDateTime start;
    private LocalDateTime end;
    private Long itemId;
}
