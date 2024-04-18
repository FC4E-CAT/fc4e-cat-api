package org.grnet.cat.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.grnet.cat.enums.ValidationStatus;
import org.grnet.cat.exceptions.InternalServerErrorException;

@Converter
public class StatusAttributeConverter implements AttributeConverter<ValidationStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(ValidationStatus status) {

        if (status == null)
            return null;

        switch (status) {
            case PENDING:
                return 1;

            case REVIEW:
                return 2;

            case APPROVED:
                return 3;

            default:
                throw new InternalServerErrorException(status + " not supported.", 501);
        }
    }

    @Override
    public ValidationStatus convertToEntityAttribute(Integer dbData) {

        if (dbData == null)
            return null;

        switch (dbData) {
            case 1:
                return ValidationStatus.PENDING;

            case 2:
                return ValidationStatus.REVIEW;

            case 3:
                return ValidationStatus.APPROVED;

            default:
                throw new InternalServerErrorException(dbData + " not supported.", 501);
        }
    }
}
