package ru.practicum.shareit.request;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.controller.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestDTO;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
public class RequestControllerTest {

    private static final String USER_ID = "X-Sharer-User-Id";
    private static final String VALID_USER_ID = "1";
    private static final String INVALID_USER_ID = "0";
    private static final String VALID_FROM = "0";
    private static final String INVALID_FROM = "-1";
    private static final String VALID_SIZE = "1";
    private static final String INVALID_SIZE = "0";
    private final static int VALID_ID = 1;
    private final static int INVALID_ID = 0;
    private final static String VALID_DESCRIPTION = "description";
    private final static String INVALID_DESCRIPTION = "   ";

    @MockBean
    private ItemRequestService itemRequestServiceMock;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private ItemRequestDTO itemRequestDTO;
    private ItemRequest itemRequest;

    private final LocalDateTime time = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        itemRequestDTO = new ItemRequestDTO(VALID_ID, VALID_DESCRIPTION, time);
        itemRequest = new ItemRequest();
        itemRequest.setDescription(VALID_DESCRIPTION);
    }

    @Test
    @DisplayName("Create with all arguments valid")
    void create_shouldReturnItemRequestDTO_validItemRequest_validUserId() throws Exception {
        when(itemRequestServiceMock.create(any(ItemRequest.class))).thenReturn(itemRequestDTO);

        mockMvc.perform(post("/requests")
                        .header(USER_ID, VALID_USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemRequestDTO.getId()))
                .andExpect(jsonPath("$.description").value(itemRequestDTO.getDescription()));

        verify(itemRequestServiceMock, times(1)).create(any(ItemRequest.class));
        verifyNoMoreInteractions(itemRequestServiceMock);
    }

    @Test
    @DisplayName("Create with invalid item request")
    void create_shouldReturnBadRequest_invalidItemRequest_validUserId() throws Exception {
        itemRequest.setDescription(INVALID_DESCRIPTION);

        mockMvc.perform(post("/requests")
                        .header(USER_ID, VALID_USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemRequest)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(itemRequestServiceMock);
    }

    @Test
    @DisplayName("Create with invalid item user id")
    void create_shouldReturnBadRequest_validItemRequest_invalidUserId() throws Exception {
        mockMvc.perform(post("/requests")
                        .header(USER_ID, INVALID_USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemRequest)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(itemRequestServiceMock);
    }

    @Test
    @DisplayName("Get user request with all args valid")
    void getUserRequests_shouldReturnListOfItemRequestsDTO_validUserId() throws Exception {
        List<ItemRequestDTO> requestDTOList = new ArrayList<>();
        requestDTOList.add(itemRequestDTO);

        when(itemRequestServiceMock.getUserRequests(anyInt())).thenReturn(requestDTOList);

        mockMvc.perform(get("/requests/")
                        .header(USER_ID, VALID_USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(itemRequestDTO.getId()))
                .andExpect(jsonPath("$[0].description").value(itemRequestDTO.getDescription()));

        verify(itemRequestServiceMock, times(1)).getUserRequests(anyInt());
        verifyNoMoreInteractions(itemRequestServiceMock);
    }

    @Test
    @DisplayName("Get user request with invalid user id")
    void getUserRequests_shouldReturnBadRequest_invalidUserId() throws Exception {
        mockMvc.perform(get("/requests/")
                        .header(USER_ID, INVALID_USER_ID))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(itemRequestServiceMock);
    }

    @Test
    @DisplayName("Get other request with all args valid")
    void getOtherRequest_shouldReturnListOfItemRequestDTO_validUserId_validFrom_validSize() throws Exception {
        List<ItemRequestDTO> requestDTOList = new ArrayList<>();
        requestDTOList.add(itemRequestDTO);

        when(itemRequestServiceMock.getOtherRequests(anyInt(), anyInt(), anyInt())).thenReturn(requestDTOList);

        mockMvc.perform(get("/requests/all")
                        .header(USER_ID, VALID_USER_ID)
                        .param("from", VALID_FROM)
                        .param("size", VALID_SIZE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(itemRequestDTO.getId()))
                .andExpect(jsonPath("$[0].description").value(itemRequestDTO.getDescription()));

        verify(itemRequestServiceMock, times(1)).getOtherRequests(anyInt(), anyInt(), anyInt());
        verifyNoMoreInteractions(itemRequestServiceMock);
    }

    @Test
    @DisplayName("Get other request with invalid user id")
    void getOtherRequest_shouldReturnBadRequest_invalidUserId_validFrom_validSize() throws Exception {
        mockMvc.perform(get("/requests/all")
                        .header(USER_ID, INVALID_USER_ID)
                        .param("from", VALID_FROM)
                        .param("size", VALID_SIZE))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(itemRequestServiceMock);
    }

    @Test
    @DisplayName("Get other request with invalid from param")
    void getOtherRequest_shouldReturnBadRequest_validUserId_invalidFrom_validSize() throws Exception {
        mockMvc.perform(get("/requests/all")
                        .header(USER_ID, VALID_USER_ID)
                        .param("from", INVALID_FROM)
                        .param("size", VALID_SIZE))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(itemRequestServiceMock);
    }

    @Test
    @DisplayName("Get other request with invalid size param")
    void getOtherRequest_shouldReturnBadRequest_validUserId_validFrom_invalidSize() throws Exception {
        mockMvc.perform(get("/requests/all")
                        .header(USER_ID, VALID_USER_ID)
                        .param("from", VALID_FROM)
                        .param("size", INVALID_SIZE))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(itemRequestServiceMock);
    }

    @Test
    @DisplayName("Get request with all args valid")
    void getRequest_shouldReturnItemRequestDTO_validRequestId_validUserId() throws Exception {
        when(itemRequestServiceMock.getRequest(anyInt(),anyInt())).thenReturn(itemRequestDTO);

        mockMvc.perform(get("/requests/" + VALID_ID)
                        .header(USER_ID, VALID_USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemRequestDTO.getId()))
                .andExpect(jsonPath("$.description").value(itemRequestDTO.getDescription()));

        verify(itemRequestServiceMock, times(1)).getRequest(anyInt(), anyInt());
        verifyNoMoreInteractions(itemRequestServiceMock);
    }

    @Test
    @DisplayName("Get request with invalid request id")
    void getRequest_shouldReturnBadRequest_invalidRequestId_validUserId() throws Exception {
        mockMvc.perform(get("/requests/" + INVALID_ID)
                        .header(USER_ID, VALID_USER_ID))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(itemRequestServiceMock);
    }

    @Test
    @DisplayName("Get request with invalid user id")
    void getRequest_shouldReturnBadRequest_validRequestId_invalidUserId() throws Exception {
        mockMvc.perform(get("/requests/" + VALID_ID)
                        .header(USER_ID, INVALID_USER_ID))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(itemRequestServiceMock);
    }
}

