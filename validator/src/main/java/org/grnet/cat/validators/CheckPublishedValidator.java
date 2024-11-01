package org.grnet.cat.validators;

import jakarta.enterprise.inject.spi.CDI;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.grnet.cat.constraints.CheckPublished;
import org.grnet.cat.entities.registry.Motivation;
import org.grnet.cat.repositories.Repository;

import java.util.Objects;

public class CheckPublishedValidator implements ConstraintValidator<CheckPublished, Object> {

    private String message;
    private Class<? extends Repository<?, ?>> repository;
    private Boolean isPublishedPermitted;

    @Override
    public void initialize(CheckPublished constraintAnnotation) {
        this.message = constraintAnnotation.message();
        this.repository = constraintAnnotation.repository();
        this.isPublishedPermitted=constraintAnnotation.isPublishedPermitted();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (Objects.isNull(value)) {
            return true;
        }

        Repository repository = CDI.current().select(this.repository).get();
        Motivation motivation = (Motivation) repository.findById(value);
        // If motivation is found and is published, the action is not permitted

        boolean isPermitted = true;
        if(motivation!=null && motivation.getPublished()!=isPublishedPermitted){
            isPermitted=false;
        }
        StringBuilder builder = new StringBuilder();

        builder.append(message);
        builder.append(StringUtils.SPACE);
        builder.append(value);
        return isPermitted;
    }
}
