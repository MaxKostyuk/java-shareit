package ru.practicum.shareit.request.storage;

import ru.practicum.shareit.request.model.ItemRequest;

public interface ItemRequestRepositoryCustom {

    ItemRequest getItemRequestById(int id);
}
