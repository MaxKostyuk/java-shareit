package ru.practicum.shareit.request.dto;

import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ItemRequestDTO {
    private int id;
    private String description;
    private LocalDateTime created;
    private List<ItemDTO> items;

    public ItemRequestDTO(int id, String description, LocalDateTime created) {
        this.id = id;
        this.description = description;
        this.created = created;
        this.items = new ArrayList<>();
    }
}
