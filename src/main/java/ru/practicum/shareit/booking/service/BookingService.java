package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDTO;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingReviewStatus;

import java.util.List;

public interface BookingService {
    BookingDTO create(Booking booking);

    BookingDTO acceptBooking(int id, int ownerId, boolean approved);

    List<BookingDTO> getById(int bookingId);

    List<BookingDTO> getByBookerId(int bookerId, BookingReviewStatus state);

    List<BookingDTO> getByOwnerId(int ownerId, BookingReviewStatus state);
}
