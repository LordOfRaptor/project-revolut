package fr.miage.revolut.services.validator;

import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Set;

@Service
class OurValidator<T> {

    private final Validator validator;

    OurValidator(Validator validator) {
        this.validator = validator;
    }

    public void validate(T obj) {
        Set<ConstraintViolation<T>> violations = validator.validate(obj);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}

