package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {

    private static final String VALID_NAME = "name";
    private static final String INVALID_NAME = "   ";
    private static final String VALID_EMAIL = "ya@ya.ru";
    private static final int VALID_USERID = 1;
    private static final int INVALID_USERID = 0;

    @MockBean
    private UserService userServiceMock;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        userDTO = new UserDTO(VALID_USERID, VALID_NAME, VALID_EMAIL);
    }

    @Test
    @DisplayName("create with valid UserDTO")
    void create_shouldReturnUserDTO_validUserDTO() throws Exception {
        when(userServiceMock.create(any(UserDTO.class))).thenReturn(userDTO);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDTO.getId()))
                .andExpect(jsonPath("$.name").value(userDTO.getName()))
                .andExpect(jsonPath("$.email").value(userDTO.getEmail()));

        verify(userServiceMock, times(1)).create(userDTO);
        verifyNoMoreInteractions(userServiceMock);
    }

    @Test
    @DisplayName("create with invalid UserDTO")
    void create_shouldReturnBadRequest_invalidUserDTO() throws Exception {
        userDTO.setName(INVALID_NAME);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(userServiceMock);
    }

    @Test
    @DisplayName("Get by id with valid id")
    void getById_shouldReturnUserDTO_validUserId() throws Exception {
        when(userServiceMock.getById(anyInt())).thenReturn(userDTO);

        mockMvc.perform(get("/users/" + VALID_USERID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDTO.getId()))
                .andExpect(jsonPath("$.name").value(userDTO.getName()))
                .andExpect(jsonPath("$.email").value(userDTO.getEmail()));

        verify(userServiceMock, times(1)).getById(VALID_USERID);
        verifyNoMoreInteractions(userServiceMock);
    }

    @Test
    @DisplayName("Get by id with invalid id")
    void getById_shouldReturnNotFound_invalidUserId() throws Exception {
        when(userServiceMock.getById(anyInt())).thenReturn(userDTO);

        mockMvc.perform(get("/users/" + INVALID_USERID))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(userServiceMock);
    }

    @Test
    @DisplayName("Get all")
    void getAll_ShouldReturnListOfUserDTO() throws Exception {
        List<UserDTO> userDTOList = new ArrayList<>();
        userDTOList.add(userDTO);

        when(userServiceMock.getAll()).thenReturn(userDTOList);

        mockMvc.perform(get("/users/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(userDTO.getId()))
                .andExpect(jsonPath("$[0].name").value(userDTO.getName()))
                .andExpect(jsonPath("$[0].email").value(userDTO.getEmail()));

        verify(userServiceMock, times(1)).getAll();
        verifyNoMoreInteractions(userServiceMock);
    }

    @Test
    @DisplayName("Update with arguments valid")
    void update_shouldReturnUserDTO_validUserId() throws Exception {
        when(userServiceMock.update(any(UserDTO.class))).thenReturn(userDTO);

        mockMvc.perform(patch("/users/" + VALID_USERID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDTO.getId()))
                .andExpect(jsonPath("$.name").value(userDTO.getName()))
                .andExpect(jsonPath("$.email").value(userDTO.getEmail()));

        verify(userServiceMock, times(1)).update(userDTO);
        verifyNoMoreInteractions(userServiceMock);
    }

    @Test
    @DisplayName("Update with invalid user id")
    void update_shouldReturnBadRequest_invalidUserId() throws Exception {
        mockMvc.perform(patch("/users/" + INVALID_USERID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(userServiceMock);
    }

    @Test
    @DisplayName("Delete with valid user id")
    void delete_shouldReturnIsOk_validUserId() throws Exception {
        mockMvc.perform(delete("/users/" + VALID_USERID))
                .andExpect(status().isOk());

        verify(userServiceMock, times(1)).delete(VALID_USERID);
        verifyNoMoreInteractions(userServiceMock);
    }

    @Test
    @DisplayName("Delete with invalid user id")
    void delete_shouldReturnBadRequest_invalidUserId() throws Exception {
        mockMvc.perform(delete("/users/" + INVALID_USERID))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(userServiceMock);
    }


}
