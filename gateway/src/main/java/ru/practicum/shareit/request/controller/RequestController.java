package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.client.RequestClient;
import ru.practicum.shareit.request.dto.RequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
@Validated
@Slf4j
public class RequestController {

    private static final String USER_ID = "X-Sharer-User-Id";

    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> createRequest(@RequestBody @Valid RequestDto requestDto,
                                                @RequestHeader(name = USER_ID) @Positive Long userId) {
        log.info("Creating request {}, userId={}", requestDto, userId);
        return requestClient.createRequest(requestDto, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getUserRequests(@RequestHeader(name = USER_ID) @Positive Long userId) {
        log.info("Getting user´s requests, userId={}", userId);
        return requestClient.getUserRequest(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getOtherRequests(@RequestHeader(name = USER_ID) @Positive Long userId,
                                                   @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                   @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Getting all requests for userId={}", userId);
        return requestClient.getAllRequests(userId, from, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getRequest(@PathVariable @Positive int id,
                                             @RequestHeader(name = USER_ID) @Positive Long userId) {
        log.info("Getting request with id={}, userId={}", id, userId);
        return requestClient.getRequest(id, userId);
    }
}
