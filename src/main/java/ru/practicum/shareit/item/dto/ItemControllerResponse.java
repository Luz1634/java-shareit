package ru.practicum.shareit.item.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class ItemControllerResponse {
    private long id;
    private String name;
    private String description;
    private boolean available;
    private ItemBookingInfoForOwner lastBooking;
    private ItemBookingInfoForOwner nextBooking;
    private List<CommentResponse> comments;
    private long requestId;
}
