package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDTO;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingReviewStatus;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

/**
 * TODO Sprint add-bookings.
 */

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {

    private static final String USER_ID = "X-Sharer-User-Id";
    private final BookingService bookingService;

    @PostMapping
    public BookingDTO create(@RequestBody @Valid Booking booking,
                             @RequestHeader(name = USER_ID) int userId) {
        booking.setBookerId(userId);
        return bookingService.create(booking);
    }

    @PatchMapping("/{id}")
    public BookingDTO acceptBooking(@PathVariable @Positive int id,
                                    @RequestParam boolean approved,
                                    @RequestHeader(name = USER_ID) @Positive int userId) {
        return bookingService.acceptBooking(id, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDTO getById(@PathVariable @Positive int bookingId,
                              @RequestHeader(name = USER_ID) @Positive int userId) {
        return bookingService.getById(bookingId, userId);
    }

    @GetMapping
    public List<BookingDTO> getByBookerId(@RequestHeader(name = USER_ID) int bookerId,
                                          @RequestParam(defaultValue = "ALL") BookingReviewStatus state) {
        return bookingService.getByBookerId(bookerId, state);
    }

    @GetMapping("/owner")
    public List<BookingDTO> getByOwnerId(@RequestParam(defaultValue = "ALL") BookingReviewStatus state,
                                         @RequestHeader(name = USER_ID) @Positive int userId) {
        return bookingService.getByOwnerId(userId, state);
    }
}
