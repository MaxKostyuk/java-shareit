package ru.practicum.shareit.request.storage;

import org.springframework.context.annotation.Lazy;
import ru.practicum.shareit.exception.ElementNotFoundException;
import ru.practicum.shareit.request.model.ItemRequest;

public class ItemRequestRepositoryCustomImpl implements ItemRequestRepositoryCustom {

    private final ItemRequestRepository itemRequestRepository;

    private static final String ITEM_REQUEST_NOT_FOUND_TEMPLATE = "Item request with id %d not found";

    public ItemRequestRepositoryCustomImpl(@Lazy ItemRequestRepository itemRequestRepository) {
        this.itemRequestRepository = itemRequestRepository;
    }

    @Override
    public ItemRequest getItemRequestById(int id) {
        return itemRequestRepository.findById(id)
                .orElseThrow(() -> new ElementNotFoundException(String.format(ITEM_REQUEST_NOT_FOUND_TEMPLATE, id)));
    }
}
