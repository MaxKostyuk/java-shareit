package ru.practicum.shareit.user.storage;

import org.springframework.context.annotation.Lazy;
import ru.practicum.shareit.exception.ElementNotFoundException;
import ru.practicum.shareit.user.model.User;

public class UserRepositoryCustomImpl implements UserRepositoryCustom{

    private final UserRepository userRepository;

    private static final String USER_NOT_FOUND_TEMPLATE = "User with id %d not found";

    public UserRepositoryCustomImpl(@Lazy UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public User getUserById(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ElementNotFoundException(String.format(USER_NOT_FOUND_TEMPLATE, id)));
    }
}
