package ru.practicum.shareit.exception.model;

public class UnavailableObjectException extends RuntimeException {
    public UnavailableObjectException(String message) {
        super(message);
    }
}
