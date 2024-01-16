package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exception.model.AddDuplicateException;
import ru.practicum.shareit.exception.model.ExceptionHandlerResponse;
import ru.practicum.shareit.exception.model.GetNonExistObjectException;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice("ru.practicum.shareit")
@Slf4j
public class ExceptionHandlerController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ExceptionHandlerResponse validation(MethodArgumentNotValidException exception) {
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
        ExceptionHandlerResponse response = new ExceptionHandlerResponse("Образение к не существующему объекту",
                exception.getMessage());
        log.warn(response.toString());
        return response;
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler
    public ExceptionHandlerResponse addDuplicate(AddDuplicateException exception) {
        ExceptionHandlerResponse response = new ExceptionHandlerResponse("Создание дубликата объекта",
                exception.getMessage());
        log.warn(response.toString());
        return response;
    }
}
