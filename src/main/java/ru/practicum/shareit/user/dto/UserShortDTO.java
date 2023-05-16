package ru.practicum.shareit.user.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class UserShortDTO {
    private int id;

    public UserShortDTO(int id) {
        this.id = id;
    }
}
