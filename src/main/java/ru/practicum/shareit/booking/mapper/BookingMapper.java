package ru.practicum.shareit.booking.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDb;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.mapper.UserMapper;

@Component
@RequiredArgsConstructor
public class BookingMapper {

    private final UserMapper userMapper;
    private final ItemMapper itemMapper;

    public Booking toBooking(BookingRequest bookingRequest) {
        Booking booking = new Booking();
        booking.setStart(bookingRequest.getStart());
        booking.setEnd(bookingRequest.getEnd());
        Item item = new Item();
        item.setId(bookingRequest.getItemId());
        booking.setItem(item);
        return booking;
    }

    public BookingResponse toBookingResponse(Booking booking) {
        BookingResponse bookingResponse = new BookingResponse();
        bookingResponse.setId(booking.getId());
        bookingResponse.setStart(booking.getStart());
        bookingResponse.setEnd(booking.getEnd());
        bookingResponse.setItem(booking.getItem());
        bookingResponse.setBooker(booking.getBooker());
        bookingResponse.setStatus(booking.getStatus());
        return bookingResponse;
    }

    public Booking toBooking(BookingDb bookingDb) {
        Booking booking = new Booking();
        booking.setId(bookingDb.getId());
        booking.setStart(bookingDb.getStart());
        booking.setEnd(bookingDb.getEnd());
        booking.setItem(itemMapper.toItem(bookingDb.getItem()));
        booking.setBooker(userMapper.toUser(bookingDb.getBooker()));
        booking.setStatus(bookingDb.getStatus());
        return booking;
    }

    public BookingDb toBookingDb(Booking booking) {
        BookingDb bookingDb = new BookingDb();
        bookingDb.setId(booking.getId());
        bookingDb.setStart(booking.getStart());
        bookingDb.setEnd(booking.getEnd());
        bookingDb.setItem(itemMapper.toItemDb(booking.getItem()));
        bookingDb.setBooker(userMapper.toUserDb(booking.getBooker()));
        bookingDb.setStatus(booking.getStatus());
        return bookingDb;
    }
}
