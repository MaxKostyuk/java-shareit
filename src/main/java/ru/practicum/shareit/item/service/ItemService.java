package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.dto.CommentDTO;
import ru.practicum.shareit.item.dto.ItemDTO;

import java.util.List;

public interface ItemService {
    ItemDTO create(ItemDTO itemDTO, int userId);

    ItemDTO getById(int id, int userId);

    List<ItemDTO> getByUser(int userId);

    List<ItemDTO> search(String text);

    ItemDTO update(ItemDTO itemDTO, int userId);

    CommentDTO addComment(Comment comment);
}
