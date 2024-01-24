package ru.practicum.shareit.exception.model;

public class GetNonExistObjectException extends RuntimeException {
    public GetNonExistObjectException(String message) {
        super(message);
    }
}
