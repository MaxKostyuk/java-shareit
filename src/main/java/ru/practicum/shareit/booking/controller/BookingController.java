package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDTO;
import ru.practicum.shareit.booking.dto.BookingResponseDTO;
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
    public BookingResponseDTO create(@RequestBody @Valid BookingDTO bookingDTO,
                                     @RequestHeader(name = USER_ID) int userId) {
        bookingDTO.setBookerId(userId);
        return bookingService.create(bookingDTO);
    }

    @PatchMapping("/{id}")
    public BookingResponseDTO acceptBooking(@PathVariable @Positive int id,
                                            @RequestParam boolean approved,
                                            @RequestHeader(name = USER_ID) @Positive int ownerId) {
        return bookingService.acceptBooking(id, ownerId, approved);
    }

    @GetMapping("/{bookingId}")
    public List<BookingResponseDTO> getById(@PathVariable @Positive int bookingId) {
        return bookingService.getById(bookingId);
    }

    @GetMapping
    public List<BookingResponseDTO> getByBookerId(@RequestHeader(name = USER_ID) int bookerId,
                                                  @RequestParam(defaultValue = "ALL") BookingReviewStatus state) {
        return bookingService.getByBookerId(bookerId, state);
    }

    @GetMapping("/owner")
    public List<BookingResponseDTO> getByOwnerId(@RequestParam(defaultValue = "ALL") BookingReviewStatus state,
                                                 @RequestHeader(name = USER_ID) @Positive int ownerId) {
        return bookingService.getByOwnerId(ownerId, state);
    }
}
