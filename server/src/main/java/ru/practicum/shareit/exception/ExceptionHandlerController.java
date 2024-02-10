package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.JDBCException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exception.model.*;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice("ru.practicum.shareit")
@Slf4j
public class ExceptionHandlerController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ExceptionHandlerResponse valid(MethodArgumentNotValidException exception) {
        List<String> exceptionMessages = new ArrayList<>();

        for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
            exceptionMessages.add(fieldError.getDefaultMessage());
        }

        ExceptionHandlerResponse response = new ExceptionHandlerResponse("Ошибка при валидации объекта",
                exceptionMessages.toString());

        log.warn(response.toString());
        return response;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ExceptionHandlerResponse validated(ConstraintViolationException exception) {
        ExceptionHandlerResponse response = new ExceptionHandlerResponse("Ошибка при валидации параметра",
                exception.getMessage());
        log.warn(response.toString());
        return response;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ExceptionHandlerResponse invalidRequest(HttpMessageNotReadableException exception) {
        ExceptionHandlerResponse response = new ExceptionHandlerResponse("Запрос составлен неправильно",
                exception.getMessage());
        log.warn(response.toString());
        return response;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ExceptionHandlerResponse missingRequestHeaders(MissingRequestHeaderException exception) {
        ExceptionHandlerResponse response = new ExceptionHandlerResponse("Отсутствует необзодимое поле Headers",
                exception.getMessage());
        log.warn(response.toString());
        return response;
    }


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
