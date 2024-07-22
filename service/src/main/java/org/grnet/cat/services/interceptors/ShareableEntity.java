package org.grnet.cat.services.interceptors;

import jakarta.enterprise.util.Nonbinding;
import jakarta.interceptor.InterceptorBinding;
import org.grnet.cat.enums.ShareableEntityType;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@InterceptorBinding
@Target({METHOD, TYPE})
@Retention(RUNTIME)
public @interface ShareableEntity {

    /**
    The type needs to be associated with a specific entity type.
     */
    @Nonbinding ShareableEntityType type();

    @Nonbinding Class<?> id();

}
