package ru.practicum.shareit.user;

public class UserMapper {

    public static UserDTO toItemDTO(User user) {
        return new UserDTO(user.getId(), user.getName(), user.getEmail());
    }
}
