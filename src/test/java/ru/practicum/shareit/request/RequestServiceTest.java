package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDTO;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RequestServiceTest {

    private static final int VALID_ID = 1;
    private static final int VALID_USER_ID = 1;
    private static final String VALID_DESCRIPTION = "description";
    @Mock
    ItemRequestRepository itemRequestRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    ItemRepository itemRepository;

    @InjectMocks
    ItemRequestServiceImpl itemRequestService;
    private ItemRequest itemRequest;
    private ItemRequestDTO itemRequestDTO;
    private ItemRequestDTO returnedRequest;
    private List<ItemRequestDTO> returnedRequests;
    private ItemDTO itemDTO;
    private final LocalDateTime time = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        itemRequest = new ItemRequest(VALID_ID, VALID_USER_ID, VALID_DESCRIPTION, time);
        itemRequestDTO = new ItemRequestDTO(VALID_ID, VALID_DESCRIPTION, time);
        itemDTO = new ItemDTO(VALID_ID, "name", "description", true, VALID_ID);
        itemRequestDTO.setItems(List.of(itemDTO));
    }

    @Test
    @DisplayName("Create all valid")
    void create_shouldReturnItemRequestDTO_allValid() {
        when(itemRequestRepository.save(any(ItemRequest.class))).thenReturn(itemRequest);

        returnedRequest = itemRequestService.create(itemRequest);

        itemRequestDTO.setItems(new ArrayList<>());
        assertEquals(returnedRequest, itemRequestDTO);
        verify(itemRequestRepository, times(1)).save(any(ItemRequest.class));
        verifyNoMoreInteractions(itemRequestRepository);
    }

    @Test
    @DisplayName("Get user requests all valid")
    void getUserRequests_shouldReturnListOfItemRequestDTO_allValid() {
        when(itemRequestRepository.getUserRequests(anyInt())).thenReturn(List.of(itemRequestDTO));
        when(itemRepository.getItemsByRequestId(anyInt())).thenReturn(List.of(itemDTO));

        returnedRequests = itemRequestService.getUserRequests(VALID_USER_ID);
        assertEquals(returnedRequests.size(), 1);
        assertEquals(returnedRequests.get(0), itemRequestDTO);

        verify(itemRequestRepository, times(1)).getUserRequests(VALID_USER_ID);
        verifyNoMoreInteractions(itemRequestRepository);
        verify(itemRepository, times(1)).getItemsByRequestId(VALID_ID);
        verifyNoMoreInteractions(itemRepository);
    }

    @Test
    @DisplayName("Get other requests all valid")
    void getOtherRequest_shouldReturnListOfItemRequestDTO_allValid() {
        when(itemRequestRepository.findAllByUserIdNot(anyInt(), any(Pageable.class))).thenReturn(List.of(itemRequest));
        when(itemRepository.getItemsByRequestId(anyInt())).thenReturn(List.of(itemDTO));

        returnedRequests = itemRequestService.getOtherRequests(VALID_USER_ID, 0, 10);
        assertEquals(returnedRequests.size(), 1);
        assertEquals(returnedRequests.get(0), itemRequestDTO);

        verify(itemRequestRepository, times(1)).findAllByUserIdNot(anyInt(), any(Pageable.class));
        verifyNoMoreInteractions(itemRequestRepository);
        verify(itemRepository, times(1)).getItemsByRequestId(VALID_ID);
        verifyNoMoreInteractions(itemRepository);
    }

    @Test
    @DisplayName("Get Request all valid")
    void getRequest_shouldReturnItemRequestDTO_allValid() {
        when(itemRequestRepository.getItemRequestById(anyInt())).thenReturn(itemRequest);
        when(itemRepository.getItemsByRequestId(anyInt())).thenReturn(List.of(itemDTO));

        returnedRequest = itemRequestService.getRequest(VALID_ID, VALID_USER_ID);
        assertEquals(returnedRequest, itemRequestDTO);
        verify(itemRequestRepository, times(1)).getItemRequestById(VALID_ID);
        verifyNoMoreInteractions(itemRequestRepository);
        verify(itemRepository, times(1)).getItemsByRequestId(VALID_ID);
        verifyNoMoreInteractions(itemRepository);
    }
}
