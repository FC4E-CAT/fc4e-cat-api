package org.grnet.cat.validators;

import io.vavr.control.Try;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.grnet.cat.constraints.NotFoundEntity;
import org.grnet.cat.exceptions.CustomValidationException;
import org.grnet.cat.repositories.Repository;

import java.util.Objects;

/**
 * This {@link NotFoundEntityValidator} defines the logic to validate the {@link NotFoundEntity}.
 * Essentially, it checks if the given Entity exists in the database.
 * If not exists, it throws a {@link CustomValidationException} with http status 404.
 */
public class NotFoundEntityValidator implements ConstraintValidator<NotFoundEntity, Object> {

    private String message;
    private Class<? extends Repository<?,?>> repository;

    @Override
    public void initialize(NotFoundEntity constraintAnnotation) {
        this.message = constraintAnnotation.message();
        this.repository = constraintAnnotation.repository();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        if(Objects.isNull(value)){
            return true;
        }

        StringBuilder builder = new StringBuilder();

        builder.append(message);
        builder.append(StringUtils.SPACE);
        builder.append(value);

        Repository repository = CDI.current().select(this.repository).get();

        Try
                .run(()->repository.searchByIdOptional(value).orElseThrow(()->new CustomValidationException(builder.toString(), 404)))
                .getOrElseThrow(()->new CustomValidationException(builder.toString(), 404));

        return true;
    }
}