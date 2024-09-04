package org.grnet.cat.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.grnet.cat.constraints.ValidUrl;

public class UrlValidator implements ConstraintValidator<ValidUrl, String> {

    // Regular expression to match URLs in the format http://url.domain or https://url.domain
    private static final String URL_PATTERN  = "^(https?:\\/\\/)?([\\da-z.-]+)\\.([a-z.]{2,6})([\\/\\w .-]*)*\\/?$";

    @Override
    public boolean isValid(String url, ConstraintValidatorContext context) {
        if (url == null) {
            return false;
        }
        return url.matches(URL_PATTERN);
    }
}
