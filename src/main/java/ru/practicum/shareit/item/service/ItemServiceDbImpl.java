package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.AccessForbiddenException;
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
    @Override
    public ItemDTO create(ItemDTO itemDTO, int userId) {
        userRepository.getUserById(userId);
        Item item = ItemMapper.toItem(itemDTO);
        item.setOwnerId(userId);
        return ItemMapper.toItemDTO(itemRepository.save(item));
    }

    @Override
    public ItemDTO getById(int id) {
        return ItemMapper.toItemDTO(itemRepository.getItemById(id));
    }

    @Override
    public List<ItemDTO> getByUser(int userId) {
        userRepository.getUserById(userId);
        return itemRepository.findByOwnerId(userId).stream().map(ItemMapper::toItemDTO).collect(Collectors.toList());
    }

    @Override
    public List<ItemDTO> search(String text) {
        return itemRepository.search(text)
                .stream().map(ItemMapper::toItemDTO).collect(Collectors.toList());
    }

    @Override
    public ItemDTO update(ItemDTO itemDTO, int userId) {
        Item itemToUpdate = itemRepository.getItemById(itemDTO.getId());
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
}
