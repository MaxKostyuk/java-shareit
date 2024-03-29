package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemShortDTO;
import ru.practicum.shareit.item.model.ItemShort;
import ru.practicum.shareit.user.dto.UserShortDTO;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@Builder
public class BookingDTO {
    private int id;
    private LocalDateTime start;
    private LocalDateTime end;
    private BookingStatus status;
    private UserShortDTO booker;
    private ItemShort item;

    public BookingDTO(int id, LocalDateTime start, LocalDateTime end, BookingStatus status,
                      int booker, int itemId, String itemName) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.status = status;
        this.booker = new UserShortDTO(booker);
        this.item = new ItemShortDTO(itemId, itemName);
    }
}
