package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.model.BookingStatus;

import javax.validation.constraints.Future;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@AllArgsConstructor
@Data
public class BookingDTO {
    @Positive
    private Integer itemId;
    @Future
    private LocalDateTime start;
    @Future
    private LocalDateTime end;
    private Integer bookerId;
    private BookingStatus status;
}
