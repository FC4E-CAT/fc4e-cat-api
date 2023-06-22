package org.grnet.cat.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.grnet.cat.constraints.StringEnumeration;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class StringEnumerationValidator implements ConstraintValidator<StringEnumeration, String> {

    private Set<String> AVAILABLE_ENUM_NAMES;
    private String message;

    @Override
    public void initialize(StringEnumeration stringEnumeration) {
        AVAILABLE_ENUM_NAMES = getNamesSet(stringEnumeration.enumClass());
        message = stringEnumeration.message();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(!check(value)){
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate("The value "+value+" is not a valid "+message+". Valid "+message+" values are: "+AVAILABLE_ENUM_NAMES)
                    .addConstraintViolation();
            return false;
        }

        return true;
    }

    private boolean check(String value){
        if ( value == null ) {
            return true;
        } else {
            return AVAILABLE_ENUM_NAMES.contains(value);
        }
    }

    private Set<String> getNamesSet(Class<? extends Enum<?>> e) {
        Enum<?>[] enums = e.getEnumConstants();
        String[] names = new String[enums.length];
        for (int i = 0; i < enums.length; i++) {
            names[i] = enums[i].name();
        }
        Set<String> mySet = new HashSet<String>(Arrays.asList(names));
        return mySet;
    }
}
