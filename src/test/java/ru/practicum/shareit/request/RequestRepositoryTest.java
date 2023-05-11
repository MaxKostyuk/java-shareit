package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.request.dto.ItemRequestDTO;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class RequestRepositoryTest {

    private static final int USER_ID = 1;
    private static final String DESCRIPTION = "description";
    @Autowired
    private ItemRequestRepository requestRepository;

    private ItemRequest itemRequest;
    private ItemRequestDTO itemRequestDTO;
    private List<ItemRequestDTO> returnedRequests;
    private final LocalDateTime created = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        itemRequest = new ItemRequest();
        itemRequest.setUserId(USER_ID);
        itemRequest.setDescription(DESCRIPTION);
        itemRequest.setCreated(created);
    }

    @Test
    @DisplayName("Get user request all valid")
    void getUserRequest_shouldReturnListOfItemRequestDTO_allValid() {
        itemRequestDTO = ItemRequestMapper.toItemRequestDTO(requestRepository.save(itemRequest));

        returnedRequests = requestRepository.getUserRequests(USER_ID);
        returnedRequests.get(0).setCreated(created);

        assertEquals(returnedRequests.size(), 1);
        assertEquals(returnedRequests.get(0), itemRequestDTO);
    }
}
