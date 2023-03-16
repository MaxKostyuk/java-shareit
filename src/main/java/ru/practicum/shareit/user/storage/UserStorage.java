package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {

    User create(User user);

    Optional<User> getById(int id);

    List<User> getAll();

    User update(User user);

    void delete(int id);

    boolean checkUser(int id);
}
