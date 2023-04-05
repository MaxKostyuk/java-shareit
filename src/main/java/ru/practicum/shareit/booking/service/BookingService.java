package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDTO;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingService {
    BookingDTO create(Booking booking);

    BookingDTO acceptBooking(int id, int userId, boolean approved);

    BookingDTO getById(int bookingId, int userId);

    List<BookingDTO> getByBookerId(int userId, String state);

    List<BookingDTO> getByOwnerId(int ownerId, String state);
}
