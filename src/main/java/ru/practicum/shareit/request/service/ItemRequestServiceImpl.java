package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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

    @Override
    public ItemRequestDTO create(ItemRequest itemRequest) {
        userRepository.getUserById(itemRequest.getUserId());
        return ItemRequestMapper.toItemRequestDTO(itemRequestRepository.save(itemRequest));
    }

    @Override
    public List<ItemRequestDTO> getUserRequests(int userId) {
        return null;
    }

    @Override
    public List<ItemRequestDTO> getOtherRequests(int userId, int from, int size) {
        return null;
    }

    @Override
    public ItemRequestDTO getRequest(int id) {
        return ItemRequestMapper.toItemRequestDTO(itemRequestRepository.getItemRequestById(id));
    }
}
