package org.grnet.cat.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.grnet.cat.repositories.Repository;
import org.grnet.cat.validators.ValidZenodoActionValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER, ElementType.FIELD, ElementType.METHOD}) // Add METHOD target
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidZenodoActionValidator.class)
public @interface ValidZenodoAction {
    String message() default "Action not permitted";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    Class<? extends Repository<?, ?>> repository();
}