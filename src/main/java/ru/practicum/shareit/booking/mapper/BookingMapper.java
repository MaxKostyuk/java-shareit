package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.user.dto.UserShortDTO;
import ru.practicum.shareit.booking.dto.BookingDTO;
import ru.practicum.shareit.booking.model.Booking;

public class BookingMapper {

    public static BookingDTO toBookingDTO(Booking booking) {
        return BookingDTO.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .booker(new UserShortDTO(booking.getBookerId()))
                .status(booking.getStatus())
                .build();
    }
}
