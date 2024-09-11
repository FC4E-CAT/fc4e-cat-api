package org.grnet.cat.entities.registry.generator;

import org.hibernate.annotations.IdGeneratorType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
@IdGeneratorType( RegistryIdGenerator.class )
@Retention(RetentionPolicy.RUNTIME)
@Target({ FIELD })
public @interface RegistryId {

}
