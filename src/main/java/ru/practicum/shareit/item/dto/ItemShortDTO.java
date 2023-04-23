package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.model.ItemShort;

@Data
@Builder
@AllArgsConstructor
public class ItemShortDTO implements ItemShort {
    private int id;
    private String name;
}
