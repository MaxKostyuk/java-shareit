package ru.practicum.shareit.booking.storage;

import ru.practicum.shareit.booking.model.Booking;

public interface BookingRepositoryCustom {

    Booking getBookingById(int id);

    boolean checkBookingForComments(int userId, int itemId);

}
