package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDTO;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public ItemRequestDTO create(ItemRequest itemRequest) {
        userRepository.getUserById(itemRequest.getUserId());
        return ItemRequestMapper.toItemRequestDTO(itemRequestRepository.save(itemRequest));
    }

    @Override
    public List<ItemRequestDTO> getUserRequests(int userId) {
        userRepository.getUserById(userId);
        List<ItemRequestDTO> requests = itemRequestRepository.getUserRequests(userId);
        for (ItemRequestDTO request : requests)
            request.setItems(itemRepository.getItemsByRequestId(request.getId()));
        return requests;
    }

    @Override
    public List<ItemRequestDTO> getOtherRequests(int userId, int from, int size) {
        return null;
    }

    @Override
    public ItemRequestDTO getRequest(int id, int userId) {
        userRepository.getUserById(userId);
        ItemRequest itemRequestById = itemRequestRepository.getItemRequestById(id);
        ItemRequestDTO request = ItemRequestMapper.toItemRequestDTO(itemRequestById);
        request.setItems(itemRepository.getItemsByRequestId(id));
        return request;
    }
}
