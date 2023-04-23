package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.dto.CommentDTO;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.dto.ItemDTO;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
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
    public ItemDTO create(@RequestBody @Valid ItemDTO itemDTO, @RequestHeader(name = USER_ID) int userId) {
        return itemService.create(itemDTO, userId);
    }

    @GetMapping("/{id}")
    public ItemDTO getById(@PathVariable @Positive int id,
                           @RequestHeader(name = USER_ID) @Positive int userId) {
        return itemService.getById(id, userId);
    }

    @GetMapping
    public List<ItemDTO> getByUser(@RequestHeader(name = USER_ID) @Positive int userId) {
        return itemService.getByUser(userId);
    }

    @GetMapping("/search")
    public List<ItemDTO> search(@RequestParam @NotBlank String text) {
        if (text.isBlank())
            return new ArrayList<>();
        return itemService.search(text);
    }

    @PatchMapping("/{id}")
    public ItemDTO update(@RequestBody ItemDTO itemDTO, @PathVariable @Positive int id,
                          @RequestHeader(name = USER_ID) @Positive int userId) {
        itemDTO.setId(id);
        return itemService.update(itemDTO, userId);
    }

    @PostMapping("/{id}/comment")
    public CommentDTO addComment(@PathVariable @Positive int id,
                                  @RequestHeader(name = USER_ID) @Positive int userId,
                                  @RequestBody @Valid Comment comment) {
        comment.setItem(id);
        comment.setAuthor(userId);
        comment.setCreated(LocalDateTime.now());
        return itemService.addComment(comment);
    }
}
