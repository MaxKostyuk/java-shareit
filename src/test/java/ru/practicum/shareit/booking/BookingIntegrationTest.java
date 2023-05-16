package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDTO;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class BookingIntegrationTest {

    @Autowired
    private BookingServiceImpl bookingService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;

    private Booking booking;
    private BookingDTO returnedBooking;
    private List<BookingDTO> returnedBookings;
    private int userId;
    private int ownerId;
    private int bookingId;

    @BeforeEach
    void setUp() {
        User owner = new User();
        owner.setName("owner name");
        owner.setEmail("valid.owner@email.com");
        ownerId = userRepository.save(owner).getId();

        User user = new User();
        user.setName("user name");
        user.setEmail("valid.user@email.com");
        userId = userRepository.save(user).getId();

        Item item = new Item();
        item.setName("item name");
        item.setDescription("item description");
        item.setAvailable(true);
        item.setOwnerId(ownerId);
        int itemId = itemRepository.save(item).getId();

        booking = Booking.builder()
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusHours(2))
                .itemId(itemId)
                .bookerId(userId)
                .build();

        bookingId = bookingService.create(booking).getId();
    }

    @Test
    void create_shouldReturnBookingDTO_allValid() {
        returnedBooking = bookingService.create(booking);

        assertEquals(booking.getItemId(), returnedBooking.getItem().getId());
        assertEquals(booking.getBookerId(), returnedBooking.getBooker().getId());
        assertEquals(booking.getStart(), returnedBooking.getStart());
        assertEquals(booking.getEnd(), returnedBooking.getEnd());
        assertEquals(BookingStatus.WAITING, returnedBooking.getStatus());
    }

    @Test
    void acceptBooking_shouldReturnBookingDTO_allValid() {
        returnedBooking = bookingService.acceptBooking(bookingId, ownerId, true);

        assertEquals(booking.getItemId(), returnedBooking.getItem().getId());
        assertEquals(booking.getBookerId(), returnedBooking.getBooker().getId());
        assertEquals(booking.getStart(), returnedBooking.getStart());
        assertEquals(booking.getEnd(), returnedBooking.getEnd());
        assertEquals(BookingStatus.APPROVED, returnedBooking.getStatus());
    }

    @Test
    void getById_shouldReturnBookingDTO_allValid() {
        returnedBooking = bookingService.getById(bookingId, userId);

        assertEquals(booking.getItemId(), returnedBooking.getItem().getId());
        assertEquals(booking.getBookerId(), returnedBooking.getBooker().getId());
        assertEquals(booking.getStart(), returnedBooking.getStart());
        assertEquals(booking.getEnd(), returnedBooking.getEnd());
        assertEquals(BookingStatus.WAITING, returnedBooking.getStatus());
    }

    @Test
    void getByBookerId_shouldReturnListOfBookingDTO_allValid() {
        returnedBookings = bookingService.getByBookerId(userId, "ALL", 0, 10);

        assertEquals(1, returnedBookings.size());
        assertEquals(booking.getItemId(), returnedBookings.get(0).getItem().getId());
        assertEquals(booking.getBookerId(), returnedBookings.get(0).getBooker().getId());
        assertEquals(BookingStatus.WAITING, returnedBookings.get(0).getStatus());
    }

    @Test
    void getByOwnerId_shouldReturnListOfBookingDTO_allValid() {
        returnedBookings = bookingService.getByOwnerId(ownerId, "ALL", 0, 10);

        assertEquals(1, returnedBookings.size());
        assertEquals(booking.getItemId(), returnedBookings.get(0).getItem().getId());
        assertEquals(booking.getBookerId(), returnedBookings.get(0).getBooker().getId());
        assertEquals(BookingStatus.WAITING, returnedBookings.get(0).getStatus());
    }
}
