package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDTO;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private static final String USER_ID = "X-Sharer-User-Id";
    private final ItemRequestService requestService;

    @PostMapping
    public ItemRequestDTO create(@RequestBody @Valid ItemRequest itemRequest,
                                 @RequestHeader(name = USER_ID) int userId) {
        itemRequest.setUserId(userId);
        itemRequest.setCreated(LocalDateTime.now());
        return requestService.create(itemRequest);
    }

    @GetMapping
    public List<ItemRequestDTO> getUserRequests(@RequestHeader(name = USER_ID) int userId) {
        return requestService.getUserRequests(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDTO> getOtherRequests(@RequestHeader(name = USER_ID) int userId,
                                                 @RequestParam int from,
                                                 @RequestParam int size) {
        return requestService.getOtherRequests(userId, from, size);
    }

    @GetMapping("/{id}")
    public ItemRequestDTO getRequest(@PathVariable int id,
                                     @RequestHeader(name = USER_ID) int userId) {
        return requestService.getRequest(id, userId);
    }
}
