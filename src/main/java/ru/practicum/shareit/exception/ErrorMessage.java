package ru.practicum.shareit.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public final class ErrorMessage {
    private final String error;

    public ErrorMessage(String message) {
        this.error = message;
    }
}
