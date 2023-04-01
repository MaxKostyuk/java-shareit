package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.ItemShort;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@Builder
public class BookingResponseDTO {
    private int id;
    private LocalDateTime start;
    private LocalDateTime end;
    private BookingStatus status;
    private BookerDTO booker;
    private ItemShort item;
}
