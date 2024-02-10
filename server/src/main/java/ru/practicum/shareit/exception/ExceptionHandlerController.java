package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.JDBCException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exception.model.*;

@RestControllerAdvice("ru.practicum.shareit")
@Slf4j
public class ExceptionHandlerController {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    public ExceptionHandlerResponse getNonExistObject(GetNonExistObjectException exception) {
        ExceptionHandlerResponse response = new ExceptionHandlerResponse("Обращение к не существующему объекту",
                exception.getMessage());
        log.warn(response.toString());
        return response;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    public ExceptionHandlerResponse nonOwnerAccess(NonOwnerAccessException exception) {
        ExceptionHandlerResponse response = new ExceptionHandlerResponse("Обращение к объекту без права доступа",
                exception.getMessage());
        log.warn(response.toString());
        return response;
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler
    public ExceptionHandlerResponse jdbcException(JDBCException exception) {
        ExceptionHandlerResponse response = new ExceptionHandlerResponse("Ошибка при работе с БД",
                exception.getSQLException().getMessage());
        log.warn(response.toString());
        return response;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ExceptionHandlerResponse unavailableObject(UnavailableObjectException exception) {
        ExceptionHandlerResponse response = new ExceptionHandlerResponse("Указанный объект не доступен",
                exception.getMessage());
        log.warn(response.toString());
        return response;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ExceptionHandlerResponse unsupportedState(UnsupportedStateException exception) {
        ExceptionHandlerResponse response = new ExceptionHandlerResponse("State указан не правильно", exception.getMessage());
        log.warn(response.toString());
        return response;
    }
}
