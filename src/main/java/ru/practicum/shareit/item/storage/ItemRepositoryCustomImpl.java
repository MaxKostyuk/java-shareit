package ru.practicum.shareit.item.storage;

import org.springframework.context.annotation.Lazy;
import ru.practicum.shareit.exception.ElementNotFoundException;
import ru.practicum.shareit.item.model.Item;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {

    private final ItemRepository itemRepository;

    private static final String ITEM_NOT_FOUND_TEMPLATE = "Item with id %d not found";

    public ItemRepositoryCustomImpl(@Lazy ItemRepository itemRepository){
        this.itemRepository = itemRepository;
    }

    @Override
    public Item getItemById(int id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new ElementNotFoundException(String.format(ITEM_NOT_FOUND_TEMPLATE, id)));
    }
}
