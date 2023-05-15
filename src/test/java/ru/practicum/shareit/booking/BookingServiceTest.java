package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.dto.BookingDTO;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.item.dto.ItemShortDTO;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.dto.UserShortDTO;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    private static final LocalDateTime VALID_START_TIME = LocalDateTime.now().plusMonths(1);
    private static final LocalDateTime VALID_END_TIME = VALID_START_TIME.plusMonths(1);
    private static final int VALID_ID = 1;
    private static final int VALID_USER_ID = 1;
    private static final int OWNER_ID = 2;

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private Booking booking;
    private BookingDTO bookingDTO;
    private BookingDTO returnedBooking;
    private List<BookingDTO> returnedList;
    private Item item;

    @BeforeEach
    void setUp() {
        booking = Booking.builder()
                .id(VALID_ID)
                .start(VALID_START_TIME)
                .end(VALID_END_TIME)
                .itemId(VALID_ID)
                .bookerId(VALID_USER_ID)
                .status(BookingStatus.WAITING)
                .build();

        bookingDTO = BookingDTO.builder()
                .id(VALID_ID)
                .start(VALID_START_TIME)
                .end(VALID_END_TIME)
                .status(BookingStatus.WAITING)
                .booker(new UserShortDTO(VALID_USER_ID))
                .item(new ItemShortDTO(VALID_ID, "Item_name"))
                .build();
        item = Item.builder()
                .id(VALID_ID)
                .name("Item_name")
                .description("Item_description")
                .available(true)
                .ownerId(OWNER_ID)
                .build();
    }

    @Test
    @DisplayName("Create all valid")
    void create_shouldReturnBookingDTO_allValid() {
        when(itemRepository.getItemById(anyInt())).thenReturn(item);
        when(bookingRepository.save(any(Booking.class)))
                .thenAnswer(invocationOnMock -> {
                    Booking booking = invocationOnMock.getArgument(0, Booking.class);
                    booking.setId(VALID_ID);
                    return booking;
                });

        returnedBooking = bookingService.create(booking);

        assertEquals(returnedBooking, bookingDTO);
        verify(itemRepository, times(1)).getItemById(VALID_ID);
        verifyNoMoreInteractions(itemRepository);
        verify(bookingRepository, times(1)).save(any(Booking.class));
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    @DisplayName("Accept booking all valid")
    void acceptBooking_shouldReturnBookingDTO_allValid() {
        when(bookingRepository.getBookingById(anyInt())).thenReturn(booking);
        when(itemRepository.getItemById(anyInt())).thenReturn(item);
        when(bookingRepository.save(any(Booking.class)))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0, Booking.class));

        returnedBooking = bookingService.acceptBooking(VALID_ID, OWNER_ID, true);

        bookingDTO.setStatus(BookingStatus.APPROVED);
        assertEquals(returnedBooking, bookingDTO);
        verify(bookingRepository, times(1)).getBookingById(VALID_ID);
        verifyNoMoreInteractions(bookingRepository);
        verify(itemRepository, times(1)).getItemById(VALID_ID);
        verifyNoMoreInteractions(itemRepository);
    }

    @Test
    @DisplayName("Get by id all valid")
    void getById_shouldReturnBookingDTO_allValid() {
        when(bookingRepository.getBookingById(anyInt())).thenReturn(booking);
        when(itemRepository.getItemById(anyInt())).thenReturn(item);

        returnedBooking = bookingService.getById(VALID_ID, OWNER_ID);

        assertEquals(returnedBooking, bookingDTO);
        verify(bookingRepository, times(1)).getBookingById(anyInt());
        verifyNoMoreInteractions(bookingRepository);
        verify(itemRepository, times(1)).getItemById(anyInt());
        verifyNoMoreInteractions(itemRepository);
    }

    @Test
    @DisplayName("Get all by booker id all valid")
    void getByBookerId_all_shouldReturnListOfBookingDTO_allValid() {
        when(bookingRepository.getAllByBookerId(anyInt(), any(Pageable.class)))
                .thenReturn(List.of(bookingDTO));

        returnedList = bookingService.getByBookerId(VALID_USER_ID, "ALL", 0, 10);

        assertEquals(returnedList.size(), 1);
        assertEquals(returnedList.get(0), bookingDTO);

        verify(bookingRepository, times(1)).getAllByBookerId(anyInt(), any(Pageable.class));
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    @DisplayName("Get current by booker id all valid")
    void getByBookerId_current_shouldReturnListOfBookingDTO_allValid() {
        when(bookingRepository.getCurrentByBookerId(anyInt(), any(Pageable.class)))
                .thenReturn(List.of(bookingDTO));

        returnedList = bookingService.getByBookerId(VALID_USER_ID, "CURRENT", 0, 10);

        assertEquals(returnedList.size(), 1);
        assertEquals(returnedList.get(0), bookingDTO);

        verify(bookingRepository, times(1)).getCurrentByBookerId(anyInt(), any(Pageable.class));
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    @DisplayName("Get past by booker id all valid")
    void getByBookerId_past_shouldReturnListOfBookingDTO_allValid() {
        when(bookingRepository.getPastByBookerId(anyInt(), any(Pageable.class)))
                .thenReturn(List.of(bookingDTO));

        returnedList = bookingService.getByBookerId(VALID_USER_ID, "PAST", 0, 10);

        assertEquals(returnedList.size(), 1);
        assertEquals(returnedList.get(0), bookingDTO);

        verify(bookingRepository, times(1)).getPastByBookerId(anyInt(), any(Pageable.class));
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    @DisplayName("Get future by booker id all valid")
    void getByBookerId_future_shouldReturnListOfBookingDTO_allValid() {
        when(bookingRepository.getFutureByBookerId(anyInt(), any(Pageable.class)))
                .thenReturn(List.of(bookingDTO));

        returnedList = bookingService.getByBookerId(VALID_USER_ID, "FUTURE", 0, 10);

        assertEquals(returnedList.size(), 1);
        assertEquals(returnedList.get(0), bookingDTO);

        verify(bookingRepository, times(1)).getFutureByBookerId(anyInt(), any(Pageable.class));
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    @DisplayName("Get waiting by booker id all valid")
    void getByBookerId_waiting_shouldReturnListOfBookingDTO_allValid() {
        when(bookingRepository.getByStatusAndBookerId(anyInt(), any(BookingStatus.class), any(Pageable.class)))
                .thenReturn(List.of(bookingDTO));

        returnedList = bookingService.getByBookerId(VALID_USER_ID, "WAITING", 0, 10);

        assertEquals(returnedList.size(), 1);
        assertEquals(returnedList.get(0), bookingDTO);

        verify(bookingRepository, times(1)).getByStatusAndBookerId(anyInt(), any(BookingStatus.class), any(Pageable.class));
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    @DisplayName("Get all by owner id all valid")
    void getByOwnerId_all_shouldReturnListOfBookingDTO_allValid() {
        when(bookingRepository.getAllByOwnerId(anyInt(), any(Pageable.class)))
                .thenReturn(List.of(bookingDTO));

        returnedList = bookingService.getByOwnerId(VALID_USER_ID, "ALL", 0, 10);

        assertEquals(returnedList.size(), 1);
        assertEquals(returnedList.get(0), bookingDTO);

        verify(bookingRepository, times(1)).getAllByOwnerId(anyInt(), any(Pageable.class));
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    @DisplayName("Get current by owner id all valid")
    void getByOwnerId_current_shouldReturnListOfBookingDTO_allValid() {
        when(bookingRepository.getCurrentByOwnerId(anyInt(), any(Pageable.class)))
                .thenReturn(List.of(bookingDTO));

        returnedList = bookingService.getByOwnerId(VALID_USER_ID, "CURRENT", 0, 10);

        assertEquals(returnedList.size(), 1);
        assertEquals(returnedList.get(0), bookingDTO);

        verify(bookingRepository, times(1)).getCurrentByOwnerId(anyInt(), any(Pageable.class));
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    @DisplayName("Get past by owner id all valid")
    void getByOwnerId_past_shouldReturnListOfBookingDTO_allValid() {
        when(bookingRepository.getPastByOwnerId(anyInt(), any(Pageable.class)))
                .thenReturn(List.of(bookingDTO));

        returnedList = bookingService.getByOwnerId(VALID_USER_ID, "PAST", 0, 10);

        assertEquals(returnedList.size(), 1);
        assertEquals(returnedList.get(0), bookingDTO);

        verify(bookingRepository, times(1)).getPastByOwnerId(anyInt(), any(Pageable.class));
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    @DisplayName("Get future by owner id all valid")
    void getByOwnerId_future_shouldReturnListOfBookingDTO_allValid() {
        when(bookingRepository.getFutureByOwnerId(anyInt(), any(Pageable.class)))
                .thenReturn(List.of(bookingDTO));

        returnedList = bookingService.getByOwnerId(VALID_USER_ID, "FUTURE", 0, 10);

        assertEquals(returnedList.size(), 1);
        assertEquals(returnedList.get(0), bookingDTO);

        verify(bookingRepository, times(1)).getFutureByOwnerId(anyInt(), any(Pageable.class));
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    @DisplayName("Get waiting by owner id all valid")
    void getByOwnerId_waiting_shouldReturnListOfBookingDTO_allValid() {
        when(bookingRepository.getByStatusAndOwnerId(anyInt(), any(BookingStatus.class), any(Pageable.class)))
                .thenReturn(List.of(bookingDTO));

        returnedList = bookingService.getByOwnerId(VALID_USER_ID, "WAITING", 0, 10);

        assertEquals(returnedList.size(), 1);
        assertEquals(returnedList.get(0), bookingDTO);

        verify(bookingRepository, times(1)).getByStatusAndOwnerId(anyInt(), any(BookingStatus.class), any(Pageable.class));
        verifyNoMoreInteractions(bookingRepository);
    }
}
