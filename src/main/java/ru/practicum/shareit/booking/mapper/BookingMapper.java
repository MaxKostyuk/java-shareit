package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookerDTO;
import ru.practicum.shareit.booking.dto.BookingDTO;
import ru.practicum.shareit.booking.dto.BookingResponseDTO;
import ru.practicum.shareit.booking.model.Booking;

public class BookingMapper {

    public static Booking toBooking(BookingDTO bookingDTO) {
        return Booking.builder()
                .start(bookingDTO.getStart())
                .end(bookingDTO.getEnd())
                .item(bookingDTO.getItemId())
                .bookerId(bookingDTO.getBookerId())
                .status(bookingDTO.getStatus())
                .build();
    }

    public static BookingResponseDTO bookingResponseDTO(Booking booking) {
        return BookingResponseDTO.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .booker(new BookerDTO(booking.getBookerId()))
                .status(booking.getStatus())
                .build();
    }
}
