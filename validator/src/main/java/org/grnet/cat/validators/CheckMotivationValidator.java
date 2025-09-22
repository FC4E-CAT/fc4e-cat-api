package org.grnet.cat.validators;

import jakarta.enterprise.inject.spi.CDI;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.grnet.cat.constraints.CheckMotivations;
import org.grnet.cat.repositories.Repository;

import java.util.ArrayList;
import java.util.List;


public class CheckMotivationValidator implements ConstraintValidator<CheckMotivations, List<String>> {

    private Class<? extends Repository<?, ?>> repository;

    @Override
    public void initialize(CheckMotivations constraintAnnotation) {
        this.repository = constraintAnnotation.repository();
    }

    @Override
    public boolean isValid(List<String> value, ConstraintValidatorContext context) {
        System.out.println("it is here ");
        if (value == null || value.isEmpty()) {
            return true; // no motivations → nothing to check
        }

        Repository repositoryBean = CDI.current().select(this.repository).get();
        List<String> notExistingIds = new ArrayList<>();

        for (String id : value) {
            if (repositoryBean.findById(id) == null) {
                notExistingIds.add(id);
            }
        }

        if (!notExistingIds.isEmpty()) {
            String message = "Motivation(s) not found with ID(s): " + String.join(", ", notExistingIds);

            // disable default and add custom message
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message)
                    .addConstraintViolation();

            return false; // ❌ triggers ConstraintViolationException
        }

        return true; // ✅ all good
    }
}
