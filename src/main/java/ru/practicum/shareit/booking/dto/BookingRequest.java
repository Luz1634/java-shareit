package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.booking.validation.DateEndAfterStart;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
// Validation
@DateEndAfterStart(message = "Дата старта и конца указана неверно")
public class BookingRequest {
    @NotNull(message = "ItemId пустой или null")
    @Min(value = 1, message = "ItemId должно быть больше 0")
    private Long itemId;
    @NotNull(message = "Start пустой или null")
    @FutureOrPresent(message = "Start указан в прошлом")
    private LocalDateTime start;
    @NotNull(message = "Start пустой или null")
    private LocalDateTime end;
}
