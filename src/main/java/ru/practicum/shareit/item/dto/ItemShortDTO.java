package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.model.ItemShort;

@Data
@Builder
public class ItemShortDTO implements ItemShort {
    private int id;
    private String name;
}
