package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.AccessForbiddenException;
import ru.practicum.shareit.exception.ElementNotFoundException;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceDbImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    private static final String ITEM_NOT_FOUND_TEMPLATE = "Item with id %d not found";
    private static final String USER_NOT_FOUND_TEMPLATE = "User with id %d not found";

    @Override
    public ItemDTO create(ItemDTO itemDTO, int userId) {
        validateUser(userId);
        Item item = ItemMapper.toItem(itemDTO);
        item.setOwnerId(userId);
        return ItemMapper.toItemDTO(itemRepository.save(item));
    }

    @Override
    public ItemDTO getById(int id) {
        return ItemMapper.toItemDTO(getItemById(id));
    }


    @Override
    public List<ItemDTO> getByUser(int userId) {
        validateUser(userId);
        return itemRepository.findByOwnerId(userId).stream().map(ItemMapper::toItemDTO).collect(Collectors.toList());
    }

    @Override
    public List<ItemDTO> search(String text) {
        return itemRepository.search(text)
                .stream().map(ItemMapper::toItemDTO).collect(Collectors.toList());
    }

    @Override
    public ItemDTO update(ItemDTO itemDTO, int userId) {
        Item itemToUpdate = getItemById(itemDTO.getId());
        if (itemToUpdate.getOwnerId() != userId)
            throw new AccessForbiddenException();
        if (Objects.nonNull(itemDTO.getName()))
            itemToUpdate.setName(itemDTO.getName());
        if (Objects.nonNull(itemDTO.getDescription()))
            itemToUpdate.setDescription(itemDTO.getDescription());
        if (Objects.nonNull(itemDTO.getAvailable()))
            itemToUpdate.setAvailable(itemDTO.getAvailable());
        return ItemMapper.toItemDTO(itemRepository.save(itemToUpdate));
    }

    private Item getItemById(int id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new ElementNotFoundException(String.format(ITEM_NOT_FOUND_TEMPLATE, id)));
    }

    private void validateUser(int userId) {
        if (!userRepository.existsById(userId))
            throw new ElementNotFoundException(String.format(USER_NOT_FOUND_TEMPLATE, userId));
    }
}
