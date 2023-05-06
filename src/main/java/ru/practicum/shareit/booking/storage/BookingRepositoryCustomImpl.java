package ru.practicum.shareit.booking.storage;

import org.springframework.context.annotation.Lazy;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.ElementNotFoundException;

public class BookingRepositoryCustomImpl implements BookingRepositoryCustom {

    private final BookingRepository bookingRepository;

    private static final String BOOKING_NOT_FOUND_TEMPLATE = "Booking with id %d not found";

    public BookingRepositoryCustomImpl(@Lazy BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    public Booking getBookingById(int id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new ElementNotFoundException(String.format(BOOKING_NOT_FOUND_TEMPLATE, id)));
    }

    @Override
    public boolean checkBookingForComments(int userId, int itemId) {
        return !bookingRepository.getPastBookingsForUserAndItem(userId, itemId).isEmpty();
    }
}
