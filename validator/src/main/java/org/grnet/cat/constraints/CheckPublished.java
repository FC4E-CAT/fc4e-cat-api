package org.grnet.cat.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.grnet.cat.repositories.Repository;
import org.grnet.cat.validators.CheckPublishedValidator;

import java.lang.annotation.*;


@Constraint(validatedBy = CheckPublishedValidator.class)
@Target({ElementType.FIELD,  ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CheckPublished {


    String message() default "Action  permitted";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    Class<? extends Repository<?,?>> repository();
    boolean isPublishedPermitted()  default false; // Default to checking if published
    String populatedBy() default "";

}