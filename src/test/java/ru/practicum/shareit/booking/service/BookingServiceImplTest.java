package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.model.GetNonExistObjectException;
import ru.practicum.shareit.exception.model.NonOwnerAccessException;
import ru.practicum.shareit.exception.model.UnavailableObjectException;
import ru.practicum.shareit.exception.model.UnsupportedStateException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingRepository repository;
    @Mock
    private BookingMapper mapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @InjectMocks
    private BookingServiceImpl service;

    @Test
    void getBooking_whenAllOk() {
        Booking booking = new Booking();
        booking.setBooker(new User(1, null, null));
        booking.setItem(new Item(1, null, null, null, new User(2, null, null), null, null));

        BookingResponse bookingResponse = new BookingResponse();
        bookingResponse.setBooker(booking.getBooker());
        bookingResponse.setItem(booking.getItem());

        when(repository.findById(anyLong())).thenReturn(Optional.of(booking));
        when(mapper.toBookingResponse(booking)).thenReturn(bookingResponse);

        assertEquals(bookingResponse, service.getBooking(1, 1));
    }

    @Test
    void getBooking_whenBookingNotFound() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(GetNonExistObjectException.class, () -> service.getBooking(1, 1));
    }

    @Test
    void getBooking_whenNotAccessUser() {
        Booking booking = new Booking();
        booking.setBooker(new User(1, null, null));
        booking.setItem(new Item(1, null, null, null, new User(2, null, null), null, null));

        when(repository.findById(anyLong())).thenReturn(Optional.of(booking));

        assertThrows(NonOwnerAccessException.class, () -> service.getBooking(3, 1));
    }

    @Test
    void getOwnerBookings_whenAllState() {
        Booking booking = new Booking();
        BookingResponse bookingResponse = new BookingResponse();

        when(itemRepository.findFirstByOwnerId(anyLong())).thenReturn(Optional.of(new Item()));
        when(repository.findByItem_OwnerId(anyLong(), any(Pageable.class))).thenReturn(List.of(booking, booking, booking));
        when(mapper.toBookingResponse(booking)).thenReturn(bookingResponse);

        List<BookingResponse> bookingResponses = service.getOwnerBookings(1, "ALL", 1, 1);

        assertEquals(3, bookingResponses.size());
        assertEquals(bookingResponse, bookingResponses.get(0));
        assertEquals(bookingResponse, bookingResponses.get(1));
        assertEquals(bookingResponse, bookingResponses.get(2));
    }

    @Test
    void getOwnerBookings_whenWaitingState() {
        Booking booking = new Booking();
        BookingResponse bookingResponse = new BookingResponse();

        when(itemRepository.findFirstByOwnerId(anyLong())).thenReturn(Optional.of(new Item()));
        when(repository.findByItem_OwnerIdAndStatus(anyLong(), any(BookingStatus.class), any(Pageable.class))).thenReturn(List.of(booking, booking, booking));
        when(mapper.toBookingResponse(booking)).thenReturn(bookingResponse);

        List<BookingResponse> bookingResponses = service.getOwnerBookings(1, "WAITING", 1, 1);

        assertEquals(3, bookingResponses.size());
        assertEquals(bookingResponse, bookingResponses.get(0));
        assertEquals(bookingResponse, bookingResponses.get(1));
        assertEquals(bookingResponse, bookingResponses.get(2));
    }

    @Test
    void getOwnerBookings_whenRejectedState() {
        Booking booking = new Booking();
        BookingResponse bookingResponse = new BookingResponse();

        when(itemRepository.findFirstByOwnerId(anyLong())).thenReturn(Optional.of(new Item()));
        when(repository.findByItem_OwnerIdAndStatus(anyLong(), any(BookingStatus.class), any(Pageable.class))).thenReturn(List.of(booking, booking, booking));
        when(mapper.toBookingResponse(booking)).thenReturn(bookingResponse);

        List<BookingResponse> bookingResponses = service.getOwnerBookings(1, "REJECTED", 1, 1);

        assertEquals(3, bookingResponses.size());
        assertEquals(bookingResponse, bookingResponses.get(0));
        assertEquals(bookingResponse, bookingResponses.get(1));
        assertEquals(bookingResponse, bookingResponses.get(2));
    }

    @Test
    void getOwnerBookings_whenPastState() {
        Booking booking = new Booking();
        BookingResponse bookingResponse = new BookingResponse();

        when(itemRepository.findFirstByOwnerId(anyLong())).thenReturn(Optional.of(new Item()));
        when(repository.findByItem_OwnerIdAndEndBefore(anyLong(), any(LocalDateTime.class), any(Pageable.class))).thenReturn(List.of(booking, booking, booking));
        when(mapper.toBookingResponse(booking)).thenReturn(bookingResponse);

        List<BookingResponse> bookingResponses = service.getOwnerBookings(1, "PAST", 1, 1);

        assertEquals(3, bookingResponses.size());
        assertEquals(bookingResponse, bookingResponses.get(0));
        assertEquals(bookingResponse, bookingResponses.get(1));
        assertEquals(bookingResponse, bookingResponses.get(2));
    }

    @Test
    void getOwnerBookings_whenCurrentState() {
        Booking booking = new Booking();
        BookingResponse bookingResponse = new BookingResponse();

        when(itemRepository.findFirstByOwnerId(anyLong())).thenReturn(Optional.of(new Item()));
        when(repository.findByItem_OwnerIdAndStartBeforeAndEndAfter(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class), any(Pageable.class))).thenReturn(List.of(booking, booking, booking));
        when(mapper.toBookingResponse(booking)).thenReturn(bookingResponse);

        List<BookingResponse> bookingResponses = service.getOwnerBookings(1, "CURRENT", 1, 1);

        assertEquals(3, bookingResponses.size());
        assertEquals(bookingResponse, bookingResponses.get(0));
        assertEquals(bookingResponse, bookingResponses.get(1));
        assertEquals(bookingResponse, bookingResponses.get(2));
    }

    @Test
    void getOwnerBookings_whenFutureState() {
        Booking booking = new Booking();
        BookingResponse bookingResponse = new BookingResponse();

        when(itemRepository.findFirstByOwnerId(anyLong())).thenReturn(Optional.of(new Item()));
        when(repository.findByItem_OwnerIdAndStartAfter(anyLong(), any(LocalDateTime.class), any(Pageable.class))).thenReturn(List.of(booking, booking, booking));
        when(mapper.toBookingResponse(booking)).thenReturn(bookingResponse);

        List<BookingResponse> bookingResponses = service.getOwnerBookings(1, "FUTURE", 1, 1);

        assertEquals(3, bookingResponses.size());
        assertEquals(bookingResponse, bookingResponses.get(0));
        assertEquals(bookingResponse, bookingResponses.get(1));
        assertEquals(bookingResponse, bookingResponses.get(2));
    }

    @Test
    void getOwnerBookings_whenUserNotFound() {
        when(itemRepository.findFirstByOwnerId(anyLong())).thenReturn(Optional.empty());

        assertThrows(GetNonExistObjectException.class, () -> service.getOwnerBookings(1, "", 1, 1));
    }

    @Test
    void getOwnerBookings_whenUnknownState() {
        when(itemRepository.findFirstByOwnerId(anyLong())).thenReturn(Optional.of(new Item()));

        assertThrows(UnsupportedStateException.class, () -> service.getOwnerBookings(1, "state", 1, 1));
    }

    @Test
    void getUserBookings_whenAllState() {
        Booking booking = new Booking();
        BookingResponse bookingResponse = new BookingResponse();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(repository.findByBookerId(anyLong(), any(Pageable.class))).thenReturn(List.of(booking, booking, booking));
        when(mapper.toBookingResponse(booking)).thenReturn(bookingResponse);

        List<BookingResponse> bookingResponses = service.getUserBookings(1, "ALL", 1, 1);

        assertEquals(3, bookingResponses.size());
        assertEquals(bookingResponse, bookingResponses.get(0));
        assertEquals(bookingResponse, bookingResponses.get(1));
        assertEquals(bookingResponse, bookingResponses.get(2));
    }

    @Test
    void getUserBookings_whenWaitingState() {
        Booking booking = new Booking();
        BookingResponse bookingResponse = new BookingResponse();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(repository.findByBookerIdAndStatus(anyLong(), any(BookingStatus.class), any(Pageable.class))).thenReturn(List.of(booking, booking, booking));
        when(mapper.toBookingResponse(booking)).thenReturn(bookingResponse);

        List<BookingResponse> bookingResponses = service.getUserBookings(1, "WAITING", 1, 1);

        assertEquals(3, bookingResponses.size());
        assertEquals(bookingResponse, bookingResponses.get(0));
        assertEquals(bookingResponse, bookingResponses.get(1));
        assertEquals(bookingResponse, bookingResponses.get(2));
    }

    @Test
    void getUserBookings_whenRejectedState() {
        Booking booking = new Booking();
        BookingResponse bookingResponse = new BookingResponse();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(repository.findByBookerIdAndStatus(anyLong(), any(BookingStatus.class), any(Pageable.class))).thenReturn(List.of(booking, booking, booking));
        when(mapper.toBookingResponse(booking)).thenReturn(bookingResponse);

        List<BookingResponse> bookingResponses = service.getUserBookings(1, "REJECTED", 1, 1);

        assertEquals(3, bookingResponses.size());
        assertEquals(bookingResponse, bookingResponses.get(0));
        assertEquals(bookingResponse, bookingResponses.get(1));
        assertEquals(bookingResponse, bookingResponses.get(2));
    }

    @Test
    void getUserBookings_whenPastState() {
        Booking booking = new Booking();
        BookingResponse bookingResponse = new BookingResponse();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(repository.findByBookerIdAndEndBefore(anyLong(), any(LocalDateTime.class), any(Pageable.class))).thenReturn(List.of(booking, booking, booking));
        when(mapper.toBookingResponse(booking)).thenReturn(bookingResponse);

        List<BookingResponse> bookingResponses = service.getUserBookings(1, "PAST", 1, 1);

        assertEquals(3, bookingResponses.size());
        assertEquals(bookingResponse, bookingResponses.get(0));
        assertEquals(bookingResponse, bookingResponses.get(1));
        assertEquals(bookingResponse, bookingResponses.get(2));
    }

    @Test
    void getUserBookings_whenCurrentState() {
        Booking booking = new Booking();
        BookingResponse bookingResponse = new BookingResponse();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(repository.findByBookerIdAndStartBeforeAndEndAfter(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class), any(Pageable.class))).thenReturn(List.of(booking, booking, booking));
        when(mapper.toBookingResponse(booking)).thenReturn(bookingResponse);

        List<BookingResponse> bookingResponses = service.getUserBookings(1, "CURRENT", 1, 1);

        assertEquals(3, bookingResponses.size());
        assertEquals(bookingResponse, bookingResponses.get(0));
        assertEquals(bookingResponse, bookingResponses.get(1));
        assertEquals(bookingResponse, bookingResponses.get(2));
    }

    @Test
    void getUserBookings_whenFutureState() {
        Booking booking = new Booking();
        BookingResponse bookingResponse = new BookingResponse();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(repository.findByBookerIdAndStartAfter(anyLong(), any(LocalDateTime.class), any(Pageable.class))).thenReturn(List.of(booking, booking, booking));
        when(mapper.toBookingResponse(booking)).thenReturn(bookingResponse);

        List<BookingResponse> bookingResponses = service.getUserBookings(1, "FUTURE", 1, 1);

        assertEquals(3, bookingResponses.size());
        assertEquals(bookingResponse, bookingResponses.get(0));
        assertEquals(bookingResponse, bookingResponses.get(1));
        assertEquals(bookingResponse, bookingResponses.get(2));
    }

    @Test
    void getUserBookings_whenUserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(GetNonExistObjectException.class, () -> service.getUserBookings(1, "", 1, 1));
    }

    @Test
    void getUserBookings_whenUnknownState() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));

        assertThrows(UnsupportedStateException.class, () -> service.getUserBookings(1, "state", 1, 1));
    }

    @Test
    void addBooking_whenAllOk() {
        User booker = new User();
        booker.setId(1);

        User owner = new User();
        owner.setId(2);

        Item item = new Item();
        item.setOwner(owner);
        item.setIsAvailable(true);

        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setItemId(1L);

        Booking booking = new Booking();

        BookingResponse bookingResponse = new BookingResponse();
        bookingResponse.setItem(item);
        bookingResponse.setBooker(booker);
        bookingResponse.setStatus(BookingStatus.WAITING);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(booker));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(mapper.toBooking(bookingRequest)).thenReturn(booking);
        when(repository.save(booking)).thenReturn(booking);
        when(mapper.toBookingResponse(booking)).thenReturn(bookingResponse);

        assertEquals(bookingResponse, service.addBooking(1, bookingRequest));
    }

    @Test
    void addBooking_whenUserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(GetNonExistObjectException.class, () -> service.addBooking(1, new BookingRequest()));
    }

    @Test
    void addBooking_whenItemNotFound() {
        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setItemId(1L);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));

        assertThrows(GetNonExistObjectException.class, () -> service.addBooking(1, bookingRequest));
    }

    @Test
    void addBooking_whenBookerIsOwner() {
        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setItemId(1L);

        User owner = new User();
        owner.setId(1);

        Item item = new Item();
        item.setOwner(owner);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(owner));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        assertThrows(GetNonExistObjectException.class, () -> service.addBooking(1, bookingRequest));
    }

    @Test
    void addBooking_whenItemIsNotAvailable() {
        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setItemId(1L);

        User owner = new User();
        owner.setId(2);

        Item item = new Item();
        item.setOwner(owner);
        item.setIsAvailable(false);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(owner));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        assertThrows(UnavailableObjectException.class, () -> service.addBooking(1, bookingRequest));
    }

    @Test
    void approveBooking_whenBookingIsApproved() {
        Booking booking = new Booking();

        User owner = new User();
        owner.setId(1);

        Item item = new Item();
        item.setOwner(owner);

        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);

        BookingResponse bookingResponse = new BookingResponse();
        bookingResponse.setItem(item);
        bookingResponse.setStatus(BookingStatus.APPROVED);

        when(repository.findById(anyLong())).thenReturn(Optional.of(booking));
        when(repository.save(booking)).thenReturn(booking);
        when(mapper.toBookingResponse(booking)).thenReturn(bookingResponse);

        assertEquals(bookingResponse, service.approveBooking(1, 1, true));
    }

    @Test
    void approveBooking_whenBookingIsNotApproved() {
        Booking booking = new Booking();

        User owner = new User();
        owner.setId(1);

        Item item = new Item();
        item.setOwner(owner);

        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);

        BookingResponse bookingResponse = new BookingResponse();
        bookingResponse.setItem(item);
        bookingResponse.setStatus(BookingStatus.REJECTED);

        when(repository.findById(anyLong())).thenReturn(Optional.of(booking));
        when(repository.save(booking)).thenReturn(booking);
        when(mapper.toBookingResponse(booking)).thenReturn(bookingResponse);

        assertEquals(bookingResponse, service.approveBooking(1, 1, false));
    }

    @Test
    void approveBooking_whenItemNotFound() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(GetNonExistObjectException.class, () -> service.approveBooking(1, 1, true));
    }

    @Test
    void approveBooking_whenUserIsNotOwner() {
        Booking booking = new Booking();

        User owner = new User();
        owner.setId(1);

        Item item = new Item();
        item.setOwner(owner);

        booking.setItem(item);

        when(repository.findById(anyLong())).thenReturn(Optional.of(booking));

        assertThrows(NonOwnerAccessException.class, () -> service.approveBooking(2, 1, true));
    }

    @Test
    void approveBooking_whenBookingIsAlreadyApproved() {
        Booking booking = new Booking();

        User owner = new User();
        owner.setId(1);

        Item item = new Item();
        item.setOwner(owner);

        booking.setItem(item);
        booking.setStatus(BookingStatus.APPROVED);

        when(repository.findById(anyLong())).thenReturn(Optional.of(booking));

        assertThrows(UnavailableObjectException.class, () -> service.approveBooking(1, 1, true));
    }
}