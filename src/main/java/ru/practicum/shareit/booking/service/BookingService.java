package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDTO;
import ru.practicum.shareit.booking.dto.BookingResponseDTO;
import ru.practicum.shareit.booking.model.BookingReviewStatus;

import java.util.List;

public interface BookingService {
    BookingResponseDTO create(BookingDTO bookingDTO);

    BookingResponseDTO acceptBooking(int id, int ownerId, boolean approved);

    List<BookingResponseDTO> getById(int bookingId);

    List<BookingResponseDTO> getByBookerId(int bookerId, BookingReviewStatus state);

    List<BookingResponseDTO> getByOwnerId(int ownerId, BookingReviewStatus state);
}
