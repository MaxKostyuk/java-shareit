package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceDbImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
public class UserIntegrationTest {
    @Autowired
    private UserServiceDbImpl userService;

    private UserDTO newUser;
    private UserDTO returnedUser;

    @BeforeEach
    void setUp() {
        newUser = new UserDTO();
        newUser.setName("name");
        newUser.setEmail("valid@email.com");
    }

    @Test
    void create_shouldReturnUserWithId1_allValid() {
        returnedUser = userService.create(newUser);

        assertEquals(newUser.getName(), returnedUser.getName());
        assertEquals(newUser.getEmail(), returnedUser.getEmail());
    }

    @Test
    void getById_shouldReturnUserDTO_allValid() {
        newUser = userService.create(newUser);

        returnedUser = userService.getById(newUser.getId());

        assertEquals(newUser, returnedUser);
    }

    @Test
    void getAll_shouldReturnListOfUserDTO_allValid() {
        newUser = userService.create(newUser);

        List<UserDTO> returnedUsers = userService.getAll();

        assertEquals(1, returnedUsers.size());
        assertEquals(newUser, returnedUsers.get(0));
    }

    @Test
    void update_shouldReturnUpdatedUserDTO_allValid() {
        newUser = userService.create(newUser);

        newUser.setName("updated_name");
        newUser.setEmail("updated@email.com");
        userService.update(newUser);

        returnedUser = userService.getById(newUser.getId());

        assertEquals(newUser, returnedUser);
    }
}
