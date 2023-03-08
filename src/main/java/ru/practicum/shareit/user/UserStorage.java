package ru.practicum.shareit.user;

import java.util.List;
import java.util.Optional;

public interface UserStorage {

    User create(User user);

    Optional<User> getById(int id);

    List<User> getAll();

    User update(User user);

    boolean delete(int id);
}
