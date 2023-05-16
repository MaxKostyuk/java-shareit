package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.dto.ItemRequestDTO;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class RequestIntegrationTest {

    @Autowired
    private ItemRequestServiceImpl requestService;
    @Autowired
    private UserService userService;

    private UserDTO createdUser;
    private ItemRequest newRequest;
    private ItemRequestDTO returnedRequest;

    @BeforeEach
    void setUp() {
        UserDTO newUser = new UserDTO();
        newUser.setName("name");
        newUser.setEmail("valid@email.com");
        createdUser = userService.create(newUser);

        newRequest = new ItemRequest();
        newRequest.setUserId(createdUser.getId());
        newRequest.setDescription("request description");
        newRequest.setCreated(LocalDateTime.now());
    }

    @Test
    void create_shouldReturnItemRequestDTO_allValid() {
        returnedRequest = requestService.create(newRequest);

        assertEquals(newRequest.getDescription(), returnedRequest.getDescription());
    }

    @Test
    void getRequest_shouldReturnItemRequestDTO_allValid() {
        int createdRequestId = requestService.create(newRequest).getId();

        returnedRequest = requestService.getRequest(createdRequestId, createdUser.getId());

        assertEquals(newRequest.getDescription(), returnedRequest.getDescription());
    }

    @Test
    void getUserRequests_shouldReturnListOfItemRequestDTO_allValid() {
        requestService.create(newRequest);
        List<ItemRequestDTO> returnedList = requestService.getUserRequests(createdUser.getId());

        assertEquals(1, returnedList.size());
        assertEquals(newRequest.getDescription(), returnedList.get(0).getDescription());
    }

    @Test
    void getOtherRequests_shouldReturnListOfItemRequestDTO_allValid() {
        requestService.create(newRequest);
        List<ItemRequestDTO> returnedList = requestService.getOtherRequests((createdUser.getId() + 1), 0, 10);

        assertEquals(1, returnedList.size());
        assertEquals(newRequest.getDescription(), returnedList.get(0).getDescription());
    }
}
