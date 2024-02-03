package ru.practicum.shareit.item.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class ItemBookingInfoForOwner {
    private long id;
    private long bookerId;
    private LocalDateTime start;
    private LocalDateTime end;
}
