package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDTO;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private static final String USER_ID = "X-Sharer-User-Id";
    private final ItemService itemService;

    @PostMapping
    public ItemDTO create(@RequestBody ItemDTO itemDTO,
                          @RequestHeader(name = USER_ID) @Positive int userId) {
        return itemService.create(itemDTO, userId);
    }

    @GetMapping("/{id}")
    public ItemDTO getById(@PathVariable @Positive int id,
                           @RequestHeader(name = USER_ID) @Positive int userId) {
        return itemService.getById(id, userId);
    }

    @GetMapping
    public List<ItemDTO> getByUser(@RequestHeader(name = USER_ID) @Positive int userId,
                                   @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                   @RequestParam(defaultValue = "10") @Positive int size) {
        return itemService.getByUser(userId, from, size);
    }

    @GetMapping("/search")
    public List<ItemDTO> search(@RequestParam @NotNull String text,
                                @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                @RequestParam(defaultValue = "10") @Positive int size) {
        if (text.isBlank())
            return new ArrayList<>();
        return itemService.search(text, from, size);
    }

    @PatchMapping("/{id}")
    public ItemDTO update(@RequestBody ItemDTO itemDTO,
                          @PathVariable @Positive int id,
                          @RequestHeader(name = USER_ID) @Positive int userId) {
        itemDTO.setId(id);
        return itemService.update(itemDTO, userId);
    }

    @PostMapping("/{id}/comment")
    public CommentDTO addComment(@PathVariable @Positive int id,
                                 @RequestHeader(name = USER_ID) @Positive int userId,
                                 @RequestBody Comment comment) {
        comment.setItem(id);
        comment.setAuthor(userId);
        comment.setCreated(LocalDateTime.now());
        return itemService.addComment(comment);
    }
}
