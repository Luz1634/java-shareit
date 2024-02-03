package ru.practicum.shareit.exception.model;

public class NonOwnerAccessException extends RuntimeException {
    public NonOwnerAccessException(String message) {
        super(message);
    }
}
