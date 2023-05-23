package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private static final String USER_ID = "X-Sharer-User-Id";

    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> bookItem(@RequestHeader(USER_ID) long userId,
                                           @RequestBody @Valid BookItemRequestDto requestDto) {
        log.info("Creating booking {}, userId={}", requestDto, userId);
        return bookingClient.bookItem(userId, requestDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> acceptBooking(@PathVariable @Positive Long id,
                                                @RequestHeader(USER_ID) @Positive Long userId,
                                                @RequestParam @NotNull Boolean approved) {
        log.info("Accepting bookingId={} by userId={}, status={}", id, userId, approved);
        return bookingClient.acceptBooking(id, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader(USER_ID) long userId,
                                             @PathVariable Long bookingId) {
        log.info("Getting booking {}, userId={}", bookingId, userId);
        return bookingClient.getBooking(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getUserBookings(@RequestHeader(USER_ID) long userId,
                                                  @RequestParam(name = "state", defaultValue = "all") String stateParam,
                                                  @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                  @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        log.info("Getting userId {} bookings with state {}, from={}, size={}", userId, stateParam, from, size);
        return bookingClient.getUserBookings(userId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getOwnerBookings(@RequestHeader(USER_ID) long userId,
                                                   @RequestParam(name = "state", defaultValue = "all") String stateParam,
                                                   @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                   @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        log.info("Getting ownerId {} bookings with state {}, from={}, size={}", userId, stateParam, from, size);
        return bookingClient.getOwnerBookings(userId, state, from, size);
    }
}
