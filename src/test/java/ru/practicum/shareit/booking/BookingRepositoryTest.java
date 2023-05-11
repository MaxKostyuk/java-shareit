package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.dto.BookingDTO;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class BookingRepositoryTest {

    private static final LocalDateTime START_TIME = LocalDateTime.now().plusHours(1);
    private static final LocalDateTime END_TIME = START_TIME.plusHours(2);
    private static final int BOOKER_ID = 123;
    public static final int OWNER_ID = 123;

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private ItemRepository itemRepository;

    private Item item;
    private Pageable page;
    private Booking booking;
    private BookingDTO bookingDTO;
    private List<BookingDTO> returnedBookings;



    @BeforeEach
    void setUp() {
        item = Item.builder()
                .name("item_name")
                .description("item_description")
                .available(true)
                .ownerId(OWNER_ID)
                .build();

        booking = Booking.builder()
                .start(START_TIME)
                .end(END_TIME)
                .bookerId(BOOKER_ID)
                .status(BookingStatus.WAITING)
                .build();


        page = PageRequest.of(0, 10, Sort.by("start").descending());
    }

    @Test
    @DisplayName("Get all by booker id all valid")
    void getAllByBookerId_shouldReturnListOfBookingDTO_allValid() {
        item = itemRepository.save(item);
        booking.setItemId(item.getId());
        booking = bookingRepository.save(booking);
        bookingDTO = BookingMapper.toBookingDTO(booking, ItemMapper.toItemShort(item));


        returnedBookings = bookingRepository.getAllByBookerId(BOOKER_ID, page);

        bookingDTO.setStart(null);
        bookingDTO.setEnd(null);
        returnedBookings.get(0).setStart(null);
        returnedBookings.get(0).setEnd(null);

        assertEquals(1, returnedBookings.size());
        assertEquals(bookingDTO, returnedBookings.get(0));
    }

    @Test
    @DisplayName("Get all by owner id all valid")
    void getAllByOwnerId_shouldReturnListOfBookingDTO_allValid() {
        item = itemRepository.save(item);
        booking.setItemId(item.getId());
        booking = bookingRepository.save(booking);
        bookingDTO = BookingMapper.toBookingDTO(booking, ItemMapper.toItemShort(item));


        returnedBookings = bookingRepository.getAllByOwnerId(OWNER_ID, page);

        bookingDTO.setStart(null);
        bookingDTO.setEnd(null);
        returnedBookings.get(0).setStart(null);
        returnedBookings.get(0).setEnd(null);

        assertEquals(1, returnedBookings.size());
        assertEquals(bookingDTO, returnedBookings.get(0));
    }

    @Test
    @DisplayName("Get current by booker id")
    void getCurrentByBookerId_shouldReturnListOfBookingDTO_allValid() throws InterruptedException {
        item = itemRepository.save(item);
        booking.setItemId(item.getId());
        booking.setStart(LocalDateTime.now().plusSeconds(1));
        bookingDTO = BookingMapper.toBookingDTO(bookingRepository.save(booking), ItemMapper.toItemShort(item));
        Thread.sleep(1000);

        returnedBookings = bookingRepository.getCurrentByBookerId(BOOKER_ID, page);

        bookingDTO.setStart(null);
        bookingDTO.setEnd(null);
        returnedBookings.get(0).setStart(null);
        returnedBookings.get(0).setEnd(null);

        assertEquals(1, returnedBookings.size());
        assertEquals(bookingDTO, returnedBookings.get(0));
    }

    @Test
    @DisplayName("Get current by owner id")
    void getCurrentByOwnerId_shouldReturnListOfBookingDTO_allValid() throws InterruptedException {
        item = itemRepository.save(item);
        booking.setItemId(item.getId());
        booking.setStart(LocalDateTime.now().plusSeconds(1));
        bookingDTO = BookingMapper.toBookingDTO(bookingRepository.save(booking), ItemMapper.toItemShort(item));
        Thread.sleep(1000);

        returnedBookings = bookingRepository.getCurrentByOwnerId(OWNER_ID, page);

        bookingDTO.setStart(null);
        bookingDTO.setEnd(null);
        returnedBookings.get(0).setStart(null);
        returnedBookings.get(0).setEnd(null);

        assertEquals(1, returnedBookings.size());
        assertEquals(bookingDTO, returnedBookings.get(0));
    }

    @Test
    @DisplayName("Get past bookings by booker id")
    void getPastByBookerId_shouldReturnListOfBookingDTO_allValid() throws InterruptedException {
        item = itemRepository.save(item);
        booking.setItemId(item.getId());
        booking.setStart(LocalDateTime.now().plusSeconds(1));
        booking.setEnd(booking.getStart().plusNanos(100));
        bookingDTO = BookingMapper.toBookingDTO(bookingRepository.save(booking), ItemMapper.toItemShort(item));
        Thread.sleep(1000);

        returnedBookings = bookingRepository.getPastByBookerId(BOOKER_ID, page);

        bookingDTO.setStart(null);
        bookingDTO.setEnd(null);
        returnedBookings.get(0).setStart(null);
        returnedBookings.get(0).setEnd(null);

        assertEquals(1, returnedBookings.size());
        assertEquals(bookingDTO, returnedBookings.get(0));
    }

    @Test
    @DisplayName("Get past bookings by owner id")
    void getPastByOwnerId_shouldReturnListOfBookingDTO_allValid() throws InterruptedException {
        item = itemRepository.save(item);
        booking.setItemId(item.getId());
        booking.setStart(LocalDateTime.now().plusSeconds(1));
        booking.setEnd(booking.getStart().plusNanos(100));
        bookingDTO = BookingMapper.toBookingDTO(bookingRepository.save(booking), ItemMapper.toItemShort(item));
        Thread.sleep(1000);

        returnedBookings = bookingRepository.getPastByOwnerId(OWNER_ID, page);

        bookingDTO.setStart(null);
        bookingDTO.setEnd(null);
        returnedBookings.get(0).setStart(null);
        returnedBookings.get(0).setEnd(null);

        assertEquals(1, returnedBookings.size());
        assertEquals(bookingDTO, returnedBookings.get(0));
    }

    @Test
    @DisplayName("Get future bookings by booker id all valid")
    void getFutureByBookerId_shouldReturnListOfBookingDTO_allValid() {
        item = itemRepository.save(item);
        booking.setItemId(item.getId());
        booking = bookingRepository.save(booking);
        bookingDTO = BookingMapper.toBookingDTO(booking, ItemMapper.toItemShort(item));


        returnedBookings = bookingRepository.getFutureByBookerId(BOOKER_ID, page);

        bookingDTO.setStart(null);
        bookingDTO.setEnd(null);
        returnedBookings.get(0).setStart(null);
        returnedBookings.get(0).setEnd(null);

        assertEquals(1, returnedBookings.size());
        assertEquals(bookingDTO, returnedBookings.get(0));
    }

    @Test
    @DisplayName("Get future bookings by owner id all valid")
    void getFutureByOwnerId_shouldReturnListOfBookingDTO_allValid() {
        item = itemRepository.save(item);
        booking.setItemId(item.getId());
        booking = bookingRepository.save(booking);
        bookingDTO = BookingMapper.toBookingDTO(booking, ItemMapper.toItemShort(item));


        returnedBookings = bookingRepository.getFutureByOwnerId(OWNER_ID, page);

        bookingDTO.setStart(null);
        bookingDTO.setEnd(null);
        returnedBookings.get(0).setStart(null);
        returnedBookings.get(0).setEnd(null);

        assertEquals(1, returnedBookings.size());
        assertEquals(bookingDTO, returnedBookings.get(0));
    }

    @Test
    @DisplayName("Get by status and booker id all valid")
    void getByStatusAndBookerId_shouldReturnListOfBookingDTO_allValid() {
        item = itemRepository.save(item);
        booking.setItemId(item.getId());
        booking = bookingRepository.save(booking);
        bookingDTO = BookingMapper.toBookingDTO(booking, ItemMapper.toItemShort(item));


        returnedBookings = bookingRepository.getByStatusAndBookerId(BOOKER_ID, BookingStatus.WAITING, page);

        bookingDTO.setStart(null);
        bookingDTO.setEnd(null);
        returnedBookings.get(0).setStart(null);
        returnedBookings.get(0).setEnd(null);

        assertEquals(1, returnedBookings.size());
        assertEquals(bookingDTO, returnedBookings.get(0));
    }

    @Test
    @DisplayName("Get by status and owner id all valid")
    void getByStatusAndOwnerId_shouldReturnListOfBookingDTO_allValid() {
        item = itemRepository.save(item);
        booking.setItemId(item.getId());
        booking = bookingRepository.save(booking);
        bookingDTO = BookingMapper.toBookingDTO(booking, ItemMapper.toItemShort(item));


        returnedBookings = bookingRepository.getByStatusAndOwnerId(OWNER_ID, BookingStatus.WAITING, page);

        bookingDTO.setStart(null);
        bookingDTO.setEnd(null);
        returnedBookings.get(0).setStart(null);
        returnedBookings.get(0).setEnd(null);

        assertEquals(1, returnedBookings.size());
        assertEquals(bookingDTO, returnedBookings.get(0));
    }

    @Test
    @DisplayName("Get past bookings for user and item all valid")
    void getPastBookingsForUserAndItem_shouldReturnListOfBooking_allValid() throws InterruptedException {
        item = itemRepository.save(item);
        booking.setItemId(item.getId());
        booking.setStart(LocalDateTime.now().plusSeconds(1));
        booking.setEnd(booking.getStart().plusNanos(100));
        booking = bookingRepository.save(booking);
        Thread.sleep(1000);

        List<Booking> returnedBooking = bookingRepository.getPastBookingsForUserAndItem(BOOKER_ID, item.getId());

        booking.setStart(null);
        booking.setEnd(null);
        returnedBooking.get(0).setStart(null);
        returnedBooking.get(0).setEnd(null);

        assertEquals(1, returnedBooking.size());
        assertEquals(booking, returnedBooking.get(0));
    }
}
