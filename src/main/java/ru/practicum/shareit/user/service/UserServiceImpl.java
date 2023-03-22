package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ElementNotFoundException;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final String USER_NOT_FOUND_TEMPLATE = "User with id %d not found";

    private final UserStorage userStorage;

    @Override
    public UserDTO create(UserDTO userDTO) {
        return UserMapper.toUserDTO(userStorage.create(UserMapper.toUser(userDTO)));
    }

    @Override
    public UserDTO getById(int id) {
        return userStorage.getById(id).map(UserMapper::toUserDTO)
                .orElseThrow(() -> new ElementNotFoundException(String.format(USER_NOT_FOUND_TEMPLATE, id)));
    }

    @Override
    public List<UserDTO> getAll() {
        return userStorage.getAll().stream().map(UserMapper::toUserDTO).collect(Collectors.toList());
    }

    @Override
    public UserDTO update(UserDTO userDTO) {
        return UserMapper.toUserDTO(userStorage.update(UserMapper.toUser(userDTO)));
    }

    @Override
    public void delete(int id) {
        userStorage.delete(id);
    }
}
