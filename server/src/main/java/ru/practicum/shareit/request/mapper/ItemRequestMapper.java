package ru.practicum.shareit.request.mapper;

import ru.practicum.shareit.request.dto.ItemRequestDTO;
import ru.practicum.shareit.request.model.ItemRequest;

public class ItemRequestMapper {

    public static ItemRequestDTO toItemRequestDTO(ItemRequest itemRequest) {
        return new ItemRequestDTO(itemRequest.getId(), itemRequest.getDescription(), itemRequest.getCreated());
    }
}
