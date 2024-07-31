package org.grnet.cat.enums;

import lombok.Getter;
import org.grnet.cat.exceptions.InvalidEnumValueException;

@Getter
public enum CriteriaImperative {
    MANDATORY("Mandatory"),
    OPTIONAL("Optional");

    private final String value;

    CriteriaImperative(String value) {
        this.value = value;
    }

    public static CriteriaImperative fromString(String value) {
        for (CriteriaImperative imperative : CriteriaImperative.values()) {
            if (imperative.value.equalsIgnoreCase(value)) {
                return imperative;
            }
        }
        throw new InvalidEnumValueException("CriteriaImperative", value);
    }

    @Override
    public String toString() {
        return this.value;
    }
}
