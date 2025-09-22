package org.grnet.cat.validators;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.grnet.cat.constraints.CheckPublicationStatus;

import java.util.List;
import java.util.Set;

public class CheckPublicationStatusValidator implements ConstraintValidator<CheckPublicationStatus, List<String>> {

    private static final Set<String> ALLOWED_VALUES = Set.of("published", "unpublished");

    @Override
    public boolean isValid(List<String> value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true; // empty is allowed
        }

        for (String status : value) {
            if (!ALLOWED_VALUES.contains(status.toLowerCase())) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                        "Invalid publication status: " + status + ". Allowed values are: published, unpublished."
                ).addConstraintViolation();
                return false;
            }
        }
        return true;
    }
}