package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.client.ItemClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.ArrayList;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
@Slf4j
public class ItemController {

    private static final String USER_ID = "X-Sharer-User-Id";
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestBody @Valid ItemDto itemDto,
                                             @RequestHeader(name = USER_ID) @Positive Long userId) {
        log.info("Creating item {}, userId={}", itemDto, userId);
        return itemClient.createItem(userId, itemDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getItem(@PathVariable @Positive int id,
                                          @RequestHeader(name = USER_ID) @Positive Long userId) {
        log.info("Getting item with id={}, userId={}", id, userId);
        return itemClient.getItem(userId, id);
    }

    @GetMapping
    public ResponseEntity<Object> getUserItems(@RequestHeader(name = USER_ID) @Positive Long userId,
                                               @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                               @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Getting all items for userId={} with from={}, size={}", userId, from, size);
        return itemClient.getUserItems(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestHeader(name = USER_ID) @Positive Long userId,
                                              @RequestParam @NotNull String text,
                                              @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                              @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Searching for userId={} items with text={}, from={}, size={}", userId, text, from, size);
        if (text.isBlank())
            return ResponseEntity.ok(new ArrayList<>());
        return itemClient.searchItems(userId, text, from, size);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateItem(@RequestBody ItemDto itemDto,
                                             @PathVariable @Positive int id,
                                             @RequestHeader(name = USER_ID) @Positive Long userId) {
        log.info("Updating itemId={} by userId={} with item params={}", id, userId, itemDto);
        return itemClient.updateItem(itemDto, id, userId);
    }

    @PatchMapping("/{id}/comment")
    public ResponseEntity<Object> addComment(@RequestBody @Valid CommentDto commentDto,
                                             @PathVariable @Positive int id,
                                             @RequestHeader(name = USER_ID) @Positive Long userId) {
        log.info("Creating comment={} for itemId={} by userId={}", commentDto, id, userId);
        return itemClient.addComment(id, userId, commentDto);
    }
}
