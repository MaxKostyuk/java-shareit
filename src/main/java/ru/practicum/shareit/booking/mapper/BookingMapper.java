package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingDTO;
import ru.practicum.shareit.booking.dto.BookingShortDTO;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.ItemShort;
import ru.practicum.shareit.user.dto.UserShortDTO;

public class BookingMapper {

    public static BookingDTO toBookingDTO(Booking booking, ItemShort itemShort) {
        return BookingDTO.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .booker(new UserShortDTO(booking.getBookerId()))
                .status(booking.getStatus())
                .item(itemShort)
                .build();
    }

    public static BookingShortDTO toBookingShortDTO(Booking booking) {
        return new BookingShortDTO(booking.getId(), booking.getBookerId());
    }
}
