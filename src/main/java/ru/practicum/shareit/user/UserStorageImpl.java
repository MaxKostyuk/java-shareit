package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

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
        if(user.getName() != null)
            userMap.get(user.getId()).setName(user.getName());
        if(user.getEmail() != null)
            if(userMap.values().stream().filter(u -> u.getId() != user.getId()).anyMatch(u -> u.getEmail().equals(user.getEmail())))
                throw new RuntimeException();
            else
                userMap.get(user.getId()).setEmail(user.getEmail());
        return userMap.get(user.getId());
    }

    @Override
    public void delete(int id) {
        userMap.remove(id);
    }
}
