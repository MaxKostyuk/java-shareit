package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.dto.ItemShortDTO;
import ru.practicum.shareit.item.model.Item;

public class ItemMapper {

    public static ItemDTO toItemDTO(Item item) {
        return new ItemDTO(item.getId(), item.getName(), item.getDescription(), item.getAvailable(), item.getRequestId());
    }

    public static Item toItem(ItemDTO itemDTO) {
        return Item.builder()
                .id(itemDTO.getId())
                .name(itemDTO.getName())
                .description(itemDTO.getDescription())
                .available(itemDTO.getAvailable())
                .requestId(itemDTO.getRequestId())
                .build();
    }

    public static ItemShortDTO toItemShort(Item item) {
        return ItemShortDTO.builder()
                .id(item.getId())
                .name(item.getName())
                .build();
    }
}
