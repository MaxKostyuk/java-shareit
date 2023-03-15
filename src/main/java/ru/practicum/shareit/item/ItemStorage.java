package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemStorage {
    
    Item create(Item item);

    Optional<Item> getById(int id);

    List<Item> getByUser(int userId);

    List<Item> search(String text);

    Item update(Item toItem, int userId);
}
