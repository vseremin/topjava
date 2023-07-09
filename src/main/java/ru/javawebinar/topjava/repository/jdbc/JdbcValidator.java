package ru.javawebinar.topjava.repository.jdbc;

import javax.validation.*;
import java.util.Set;

public class JdbcValidator {

    public static <T> void valid(T t) {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();

        Set<ConstraintViolation<T>> violations = validator.validate(t);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
