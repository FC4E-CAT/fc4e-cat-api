package org.grnet.cat.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.grnet.cat.validators.UrlValidator;;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UrlValidator.class)
@Documented
public @interface ValidUrl {

    String message() default "Invalid Url: must be a url format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

