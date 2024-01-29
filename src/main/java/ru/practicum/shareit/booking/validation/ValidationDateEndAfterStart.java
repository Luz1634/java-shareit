package ru.practicum.shareit.booking.validation;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import ru.practicum.shareit.booking.dto.BookingRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Order(Ordered.HIGHEST_PRECEDENCE)
public class ValidationDateEndAfterStart implements ConstraintValidator<DateEndAfterStart, BookingRequest> {

    @Override
    public void initialize(DateEndAfterStart constraintAnnotation) {
    }

    @Override
    public boolean isValid(BookingRequest booking, ConstraintValidatorContext context) {
        return booking == null ||
                booking.getEnd() == null ||
                booking.getStart() == null ||
                booking.getEnd().isAfter(booking.getStart());
    }
}
