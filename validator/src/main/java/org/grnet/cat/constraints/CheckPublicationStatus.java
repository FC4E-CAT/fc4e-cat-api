package org.grnet.cat.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.grnet.cat.validators.CheckPublicationStatusValidator;

import java.lang.annotation.*;


@Constraint(validatedBy = CheckPublicationStatusValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckPublicationStatus {


    String message() default "Invalid publication status value. Allowed values are: published, unpublished.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}