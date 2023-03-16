package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.AccessForbiddenException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ItemStorageImpl implements ItemStorage {

    private final HashMap<Integer, Item> itemMap = new HashMap<>();
    int idCounter = 0;

    @Override
    public Item create(Item item) {
        idCounter++;
        item.setId(idCounter);
        itemMap.put(idCounter, item);
        return item;
    }

    @Override
    public Optional<Item> getById(int id) {
        return Optional.ofNullable(itemMap.get(id));
    }

    @Override
    public List<Item> getByUser(int userId) {
        return itemMap.values().stream()
                .filter(i -> i.getOwnerId() == userId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> search(String text) {
        return itemMap.values().stream()
                .filter(i -> i.getDescription().toLowerCase().contains(text.toLowerCase()))
                .filter(Item::getAvailable)
                .collect(Collectors.toList());
    }

    @Override
    public Item update(Item item, int userId) {
        Item itemToUpdate = itemMap.get(item.getId());
        if (itemToUpdate.getOwnerId() != userId)
            throw new AccessForbiddenException();
        if (item.getName() != null)
            itemToUpdate.setName(item.getName());
        if (item.getDescription() != null)
            itemToUpdate.setDescription(item.getDescription());
        if (item.getAvailable() != null)
            itemToUpdate.setAvailable(item.getAvailable());
        return itemToUpdate;
    }
}
