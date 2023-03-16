package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ElementNotFoundException;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    private static final String ITEM_NOT_FOUND_TEMPLATE = "Item with id %d not found";


    public ItemDTO create(ItemDTO itemDTO, int userId) {
        userStorage.checkUser(userId);
        Item item = ItemMapper.toItem(itemDTO);
        item.setOwnerId(userId);
        return ItemMapper.toItemDTO(itemStorage.create(item));
    }

    public ItemDTO getById(int id) {
        return itemStorage.getById(id).map(ItemMapper::toItemDTO)
                .orElseThrow(() -> new ElementNotFoundException(String.format(ITEM_NOT_FOUND_TEMPLATE, id)));
    }


    public List<ItemDTO> getByUser(int userId) {
        userStorage.checkUser(userId);
        return itemStorage.getByUser(userId).stream().map(ItemMapper::toItemDTO).collect(Collectors.toList());
    }

    public List<ItemDTO> search(String text) {
        return itemStorage.search(text).stream().map(ItemMapper::toItemDTO).collect(Collectors.toList());
    }

    public ItemDTO update(ItemDTO itemDTO, int userId) {
        return ItemMapper.toItemDTO(itemStorage.update(ItemMapper.toItem(itemDTO), userId));
    }
}
