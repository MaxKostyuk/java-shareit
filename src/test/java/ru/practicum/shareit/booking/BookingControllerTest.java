package ru.practicum.shareit.booking;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingDTO;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTest {

    private static final String USER_ID = "X-Sharer-User-Id";
    private static final int VALID_USER_ID = 1;
    private static final int INVALID_USER_ID = 0;
    private static final int VALID_BOOKING_ID = 1;
    private static final int INVALID_BOOKING_ID = 0;
    private static final String SIZE_PARAM_NAME = "size";
    private static final String VALID_SIZE_PARAM = "1";
    private static final String INVALID_SIZE_PARAM = "0";
    private static final String FROM_PARAM_NAME = "from";
    private static final String VALID_FROM_PARAM = "0";
    private static final String INVALID_FROM_PARAM = "-1";

    @MockBean
    private BookingService bookingServiceMock;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Booking booking;

    private BookingDTO bookingDTO;

    private final LocalDateTime startTime = LocalDateTime.now().plusHours(1);
    private final LocalDateTime endTime = startTime.plusMinutes(1);

    @BeforeEach
    void setUp() {
        booking = Booking.builder()
                .id(1)
                .start(startTime)
                .end(endTime)
                .itemId(1)
                .build();

        bookingDTO = BookingDTO.builder()
                .id(1)
                .start(startTime)
                .end(endTime)
                .build();
    }

    @Test
    @DisplayName("Create with all arguments valid")
    void create_shouldReturnBookingDTO_validBooking_validUserId() throws Exception {
        when(bookingServiceMock.create(any(Booking.class))).thenReturn(bookingDTO);

        mockMvc.perform(post("/bookings")
                        .header(USER_ID, VALID_USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(booking)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingDTO.getId()));
        verify(bookingServiceMock, times(1)).create(any(Booking.class));
        verifyNoMoreInteractions(bookingServiceMock);
    }

    @Test
    @DisplayName("Create with invalid booking")
    void create_shouldReturnBadRequest_invalidBooking_validUserId() throws Exception {
        booking.setEnd(null);

        mockMvc.perform(post("/bookings")
                        .header(USER_ID, VALID_USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(booking)))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(bookingServiceMock);
    }

    @Test
    @DisplayName("Create with invalid user id")
    void create_shouldReturnBadRequest_validBooking_invalidUserId() throws Exception {
        mockMvc.perform(post("/bookings")
                        .header(USER_ID, INVALID_USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(booking)))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(bookingServiceMock);
    }

    @Test
    @DisplayName("Accept booking with all arguments valid")
    void acceptBooking_shouldReturnBookingDTO_validBookingId_validApproved_validUserId() throws Exception {
        when(bookingServiceMock.acceptBooking(anyInt(), anyInt(), anyBoolean())).thenReturn(bookingDTO);

        mockMvc.perform(patch("/bookings/" + VALID_BOOKING_ID)
                        .param("approved", "true")
                        .header(USER_ID, VALID_USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingDTO.getId()));
        verify(bookingServiceMock, times(1)).acceptBooking(anyInt(), anyInt(), anyBoolean());
        verifyNoMoreInteractions(bookingServiceMock);
    }

    @Test
    @DisplayName("Accept booking with invalid booking id")
    void acceptBooking_shouldReturnBadRequest_invalidBookingId_validApproved_validUserId() throws Exception {
        mockMvc.perform(patch("/bookings/" + INVALID_BOOKING_ID)
                        .param("approved", "true")
                        .header(USER_ID, VALID_USER_ID))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(bookingServiceMock);
    }

    @Test
    @DisplayName("Accept booking with no approved param")
    void acceptBooking_shouldReturnBadRequest_validBookingId_invalidApproved_validUserId() throws Exception {
        mockMvc.perform(patch("/bookings/" + VALID_BOOKING_ID)
                        .header(USER_ID, VALID_USER_ID))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(bookingServiceMock);
    }

    @Test
    @DisplayName("Accept booking with invalid user id")
    void acceptBooking_shouldReturnBadRequest_validBookingId_validApproved_invalidUserId() throws Exception {
        mockMvc.perform(patch("/bookings/" + VALID_BOOKING_ID)
                        .param("approved", "true")
                        .header(USER_ID, INVALID_USER_ID))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(bookingServiceMock);
    }

    @Test
    @DisplayName("Get by id with all arguments valid")
    void getById_shouldReturnBookingDTO_validBookingId_validUserId() throws Exception {
        when(bookingServiceMock.getById(anyInt(), anyInt())).thenReturn(bookingDTO);

        mockMvc.perform(get("/bookings/" + VALID_BOOKING_ID)
                        .header(USER_ID, VALID_USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingDTO.getId()));
        verify(bookingServiceMock, times(1)).getById(VALID_BOOKING_ID, VALID_USER_ID);
        verifyNoMoreInteractions(bookingServiceMock);
    }

    @Test
    @DisplayName("Get by id with invalid booking id")
    void getById_shouldReturnBadRequest_invalidBookingId_validUserId() throws Exception {
        mockMvc.perform(get("/bookings/" + INVALID_BOOKING_ID)
                        .header(USER_ID, VALID_USER_ID))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(bookingServiceMock);
    }

    @Test
    @DisplayName("Get by id with invalid user id")
    void getById_shouldReturnBadRequest_validBookingId_invalidUserId() throws Exception {
        mockMvc.perform(get("/bookings/" + VALID_BOOKING_ID)
                        .header(USER_ID, INVALID_USER_ID))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(bookingServiceMock);
    }

    @Test
    @DisplayName("Get by booker id with all arguments valid")
    void getByBookerId_shouldReturnListOfBookingDTO_validUserId_validFrom_validSize() throws Exception {
        List<BookingDTO> bookingDTOList = new ArrayList<>();
        bookingDTOList.add(bookingDTO);

        when(bookingServiceMock.getByBookerId(anyInt(), anyString(), anyInt(), anyInt())).thenReturn(bookingDTOList);

        mockMvc.perform(get("/bookings")
                        .header(USER_ID, VALID_USER_ID)
                        .param("state", "any")
                        .param(SIZE_PARAM_NAME, VALID_SIZE_PARAM)
                        .param(FROM_PARAM_NAME, VALID_FROM_PARAM))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(bookingDTO.getId()));

        verify(bookingServiceMock, times(1)).getByBookerId(anyInt(), anyString(), anyInt(), anyInt());
        verifyNoMoreInteractions(bookingServiceMock);
    }

    @Test
    @DisplayName("Get by booker id with invalid user id")
    void getByBookerId_shouldReturnBadRequest_invalidUserId_validFrom_validSize() throws Exception {
        mockMvc.perform(get("/bookings")
                        .header(USER_ID, INVALID_USER_ID)
                        .param("state", "any")
                        .param(SIZE_PARAM_NAME, VALID_SIZE_PARAM)
                        .param(FROM_PARAM_NAME, VALID_FROM_PARAM))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(bookingServiceMock);
    }

    @Test
    @DisplayName("Get by booker id with invalid from param")
    void getByBookerId_shouldReturnBadRequest_validUserId_invalidFrom_validSize() throws Exception {
        mockMvc.perform(get("/bookings")
                        .header(USER_ID, VALID_USER_ID)
                        .param("state", "any")
                        .param(SIZE_PARAM_NAME, VALID_SIZE_PARAM)
                        .param(FROM_PARAM_NAME, INVALID_FROM_PARAM))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(bookingServiceMock);
    }


    @Test
    @DisplayName("Get by booker id with invalid size param")
    void getByBookerId_shouldReturnBadRequest_validUserId_validFrom_invalidSize() throws Exception {
        mockMvc.perform(get("/bookings")
                        .header(USER_ID, VALID_USER_ID)
                        .param("state", "any")
                        .param(SIZE_PARAM_NAME, INVALID_SIZE_PARAM)
                        .param(FROM_PARAM_NAME, VALID_FROM_PARAM))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(bookingServiceMock);
    }

    @Test
    @DisplayName("Get by owner id with all arguments valid")
    void getByOwnerId_shouldReturnListOfBookingDTO_validUserId_validFrom_validSize() throws Exception {
        List<BookingDTO> bookingDTOList = new ArrayList<>();
        bookingDTOList.add(bookingDTO);

        when(bookingServiceMock.getByOwnerId(anyInt(), anyString(), anyInt(), anyInt())).thenReturn(bookingDTOList);

        mockMvc.perform(get("/bookings/owner")
                        .header(USER_ID, VALID_USER_ID)
                        .param("state", "any")
                        .param(SIZE_PARAM_NAME, VALID_SIZE_PARAM)
                        .param(FROM_PARAM_NAME, VALID_FROM_PARAM))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(bookingDTO.getId()));

        verify(bookingServiceMock, times(1)).getByOwnerId(anyInt(), anyString(), anyInt(), anyInt());
        verifyNoMoreInteractions(bookingServiceMock);
    }

    @Test
    @DisplayName("Get by owner id with invalid user id")
    void getByOwnerId_shouldReturnBadRequest_invalidUserId_validFrom_validSize() throws Exception {
        mockMvc.perform(get("/bookings/owner")
                        .header(USER_ID, INVALID_USER_ID)
                        .param("state", "any")
                        .param(SIZE_PARAM_NAME, VALID_SIZE_PARAM)
                        .param(FROM_PARAM_NAME, VALID_FROM_PARAM))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(bookingServiceMock);
    }

    @Test
    @DisplayName("Get by owner id with invalid from param")
    void getByOwnerId_shouldReturnBadRequest_validUserId_invalidFrom_validSize() throws Exception {
        mockMvc.perform(get("/bookings/owner")
                        .header(USER_ID, VALID_USER_ID)
                        .param("state", "any")
                        .param(SIZE_PARAM_NAME, VALID_SIZE_PARAM)
                        .param(FROM_PARAM_NAME, INVALID_FROM_PARAM))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(bookingServiceMock);
    }


    @Test
    @DisplayName("Get by owner id with invalid size param")
    void getByOwnerId_shouldReturnBadRequest_validUserId_validFrom_invalidSize() throws Exception {
        mockMvc.perform(get("/bookings/owner")
                        .header(USER_ID, VALID_USER_ID)
                        .param("state", "any")
                        .param(SIZE_PARAM_NAME, INVALID_SIZE_PARAM)
                        .param(FROM_PARAM_NAME, VALID_FROM_PARAM))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(bookingServiceMock);
    }
}
