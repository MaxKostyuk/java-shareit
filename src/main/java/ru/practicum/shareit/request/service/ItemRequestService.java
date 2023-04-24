package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDTO;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDTO create(ItemRequest itemRequest);

    List<ItemRequestDTO> getUserRequests(int userId);

    List<ItemRequestDTO> getOtherRequests(int userId, int from, int size);

    ItemRequestDTO getRequest(int id, int userId);
}
