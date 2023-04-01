package ru.practicum.shareit.booking.dto;

import lombok.Getter;

@Getter
public class BookerDTO {
    private int id;

    public BookerDTO(int id) {
        this.id = id;
    }
}
