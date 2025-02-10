package org.grnet.cat.validators;

import jakarta.enterprise.inject.spi.CDI;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.grnet.cat.constraints.ValidZenodoAction;
import org.grnet.cat.entities.MotivationAssessment;
import org.grnet.cat.repositories.Repository;

import java.util.Objects;

public class ValidZenodoActionValidator implements ConstraintValidator<ValidZenodoAction, String> {

    private String message;
    private Class<? extends Repository<MotivationAssessment, String>> repository;

    @Override
    public void initialize(ValidZenodoAction constraintAnnotation) {
        this.message = constraintAnnotation.message();
        this.repository = (Class<? extends Repository<MotivationAssessment, String>>) constraintAnnotation.repository();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (Objects.isNull(value)) {
            return false; // invalid if value is null
        }

        // Fetch the repository instance
        Repository<MotivationAssessment, String> repositoryInstance = CDI.current().select(this.repository).get();
        MotivationAssessment assessment = repositoryInstance.findById(value);

        // If no assessment is found, return false with a custom message
        if (assessment == null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Assessment not found: " + value)
                    .addConstraintViolation();
            return false;
        }

        // Check if the assessment is published and if the current user is one of the creators
        boolean isPublished = Boolean.TRUE.equals(assessment.getPublished());

        // Validation logic - only allow action if the assessment is published and user is a creator
        if (!isPublished){// || !isCreator) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message + " " + value)
                    .addConstraintViolation();
            return false;
        }

        return true; // Valid if passed the checks
    }

    private String getCurrentUser() {
        return "user@example.com"; // Replace with actual authentication logic
    }
}
