package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ElementNotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Component
public class UserStorageImpl implements UserStorage {

    private final HashMap<Integer, User> userMap = new HashMap<>();
    int idCounter = 0;

    @Override
    public User create(User user) {
        if (userMap.values().stream().map(User::getEmail).anyMatch(s -> s.equals(user.getEmail())))
            throw new RuntimeException();
        idCounter++;
        user.setId(idCounter);
        userMap.put(idCounter, user);
        return user;
    }

    @Override
    public Optional<User> getById(int id) {
        return Optional.ofNullable(userMap.get(id));
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(userMap.values());
    }

    @Override
    public User update(User user) {
        if (user.getName() != null)
            userMap.get(user.getId()).setName(user.getName());
        if (user.getEmail() != null)
            if (userMap.values().stream().filter(u -> u.getId() != user.getId()).anyMatch(u -> u.getEmail().equals(user.getEmail())))
                throw new RuntimeException();
            else
                userMap.get(user.getId()).setEmail(user.getEmail());
        return userMap.get(user.getId());
    }

    @Override
    public void delete(int id) {
        userMap.remove(id);
    }

    @Override
    public boolean checkUser(int id) {
        if (userMap.containsKey(id)) {
            return true;
        } else throw new ElementNotFoundException(String.format("User with id %d not found", id));
    }
}
