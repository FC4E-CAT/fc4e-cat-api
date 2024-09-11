package org.grnet.cat.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.grnet.cat.constraints.ValidUuid;

public class UuidValidator implements ConstraintValidator<ValidUuid, String> {

    private static final String UUID_PATTERN = "^[A-Za-z0-9]{8}$";

    @Override
    public boolean isValid(String uuid, ConstraintValidatorContext context) {
        if (uuid == null) {
            return false;
        }
        return uuid.matches(UUID_PATTERN);
    }
}
