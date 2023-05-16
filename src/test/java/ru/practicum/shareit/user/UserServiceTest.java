package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceDbImpl;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private static final String VALID_EMAIL = "email@ya.ru";
    private static final String VALID_NAME = "name";
    private static final int VALID_ID = 1;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceDbImpl userService;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    private User user;

    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        user = new User(VALID_ID, VALID_NAME, VALID_EMAIL);
        userDTO = new UserDTO(VALID_ID, VALID_NAME, VALID_EMAIL);

    }

    @Test
    @DisplayName("Create all args valid")
    void create_shouldReturnUserDTO_allValid() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDTO returnedUser = userService.create(userDTO);
        assertEquals(returnedUser, userDTO);
        verify(userRepository, times(1)).save(userArgumentCaptor.capture());
        assertEquals(userArgumentCaptor.getValue(), user);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("Get by id all valid")
    void getById_shouldReturnUserDTO_allValid() {
        when(userRepository.getUserById(anyInt())).thenReturn(user);

        UserDTO returnedUser = userService.getById(VALID_ID);
        verify(userRepository, times(1)).getUserById(VALID_ID);
        assertEquals(returnedUser, userDTO);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("Get all all valid")
    void getAll_shouldReturnListOfUserDTO_allValid() {
        List<User> usersToReturn = new ArrayList<>();
        usersToReturn.add(user);

        when(userRepository.findAll()).thenReturn(usersToReturn);

        List<UserDTO> returnedUsers = userService.getAll();
        assertEquals(returnedUsers.size(), 1);
        assertEquals(returnedUsers.get(0), userDTO);
        verify(userRepository, times(1)).findAll();
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("Update all valid")
    void update_shouldReturnUserDTO_allValid() {
        when(userRepository.getUserById(anyInt())).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);

        userDTO.setEmail("updated@email.com");
        userDTO.setName("updated name");

        UserDTO returnedUser = userService.update(userDTO);
        assertEquals(returnedUser, userDTO);
        verify(userRepository, times(1)).getUserById(userDTO.getId());
        verify(userRepository, times(1)).save(userArgumentCaptor.capture());
        verifyNoMoreInteractions(userRepository);
        assertEquals(userArgumentCaptor.getValue(), UserMapper.toUser(userDTO));
    }

    @Test
    @DisplayName("Delete all valid")
    void delete_shouldCallRepositoryOnlyOnce_allValid() {
        userService.delete(VALID_ID);

        verify(userRepository, times(1)).deleteById(VALID_ID);
        verifyNoMoreInteractions(userRepository);
    }

}
