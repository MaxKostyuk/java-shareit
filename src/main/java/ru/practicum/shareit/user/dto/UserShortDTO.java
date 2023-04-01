package ru.practicum.shareit.user.dto;

import lombok.Getter;

@Getter
public class UserShortDTO {
    private int id;

    public UserShortDTO(int id) {
        this.id = id;
    }
}
