package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ElementNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserService {

    private static final String USER_NOT_FOUND_TEMPLATE = "User with id %d not found";

    private final UserStorage userStorage;

    public UserDTO create(UserDTO userDTO) {
        return UserMapper.toUserDTO(userStorage.create(UserMapper.toUser(userDTO)));
    }

    public UserDTO getById(int id) {
        User user = userStorage.getById(id)
                .orElseThrow(() -> new ElementNotFoundException(String.format(USER_NOT_FOUND_TEMPLATE, id)));
        return UserMapper.toUserDTO(user);
    }

    public List<UserDTO> getAll() {
        return userStorage.getAll().stream().map(UserMapper::toUserDTO).collect(Collectors.toList());
    }

    public UserDTO update(UserDTO userDTO) {
        return UserMapper.toUserDTO(userStorage.update(UserMapper.toUser(userDTO)));
    }

    public void delete(int id) {
        userStorage.delete(id);
    }
}
