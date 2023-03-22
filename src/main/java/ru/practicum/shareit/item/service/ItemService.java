package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDTO;

import java.util.List;

public interface ItemService {
    ItemDTO create(ItemDTO itemDTO, int userId);

    ItemDTO getById(int id);

    List<ItemDTO> getByUser(int userId);

    List<ItemDTO> search(String text);

    ItemDTO update(ItemDTO itemDTO, int userId);
}
