package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.model.Item;

public class ItemMapper {

    public static ItemDTO toItemDTO(Item item) {
        return new ItemDTO(item.getId(), item.getName(), item.getDescription(), item.getAvailable());
    }

    public static Item toItem(ItemDTO itemDTO) {
        return new Item(itemDTO.getId(), itemDTO.getName(), itemDTO.getDescription(), itemDTO.getAvailable());
    }
}
