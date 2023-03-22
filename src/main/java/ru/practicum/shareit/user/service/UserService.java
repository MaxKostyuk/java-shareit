package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDTO;

import java.util.List;

public interface UserService {
    UserDTO create(UserDTO userDTO);

    UserDTO getById(int id);

    List<UserDTO> getAll();

    UserDTO update(UserDTO userDTO);

    void delete(int id);
}
