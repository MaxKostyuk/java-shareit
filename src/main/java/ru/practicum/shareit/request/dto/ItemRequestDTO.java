package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDTO;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ItemRequestDTO {
    private int id;
    private String description;
    private LocalDateTime created;
    List<ItemDTO> items;
}
