package org.grnet.cat.validators;

import jakarta.enterprise.inject.spi.CDI;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.grnet.cat.constraints.ValidFilters;
import org.grnet.cat.enums.PublicationStatus;
import org.grnet.cat.repositories.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class FiltersValidator implements ConstraintValidator<ValidFilters, Map<String, List<String>>> {

    private Class<? extends Repository<?, ?>> repository;

    @Override
    public void initialize(ValidFilters constraintAnnotation) {
        this.repository = constraintAnnotation.repository();
    }

    @Override
    public boolean isValid(Map<String, List<String>> filters, ConstraintValidatorContext context) {
        if (filters == null) return true;

        Repository repositoryBean = CDI.current().select(this.repository).get();
        List<String> notExistingIds = new ArrayList<>();


        boolean valid = true;

        if (filters.containsKey("motivations")) {
            for (String id : filters.get("motivations")) {
                if (repositoryBean.findById(id) == null) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate("Invalid motivation ID: " + id)
                            .addConstraintViolation();
                    valid = false;
                }
            }
        }

        // Validate publication_status (via enum)
        if (filters.containsKey("publication_status")) {
            for (String status : filters.get("publication_status")) {
                try {
                    PublicationStatus.fromId(status); // throws if invalid
                } catch (IllegalArgumentException e) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate("Invalid publication_status: " + status)
                            .addConstraintViolation();
                    valid = false;
                }
            }
        }


        return valid;
    }
}