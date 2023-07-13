package ru.javawebinar.topjava.repository.jdbc;

import javax.validation.*;
import java.util.Set;

public class Validator {

    private static javax.validation.Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public static <T> void validate(T t) {
        Set<ConstraintViolation<T>> violations = validator.validate(t);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
