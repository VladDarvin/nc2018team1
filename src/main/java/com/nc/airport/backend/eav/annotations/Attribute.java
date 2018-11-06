package com.nc.airport.backend.eav.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Used to mark attributes of an @ObjectType
 *
 * @see ObjectType
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface Attribute {
    int value();
}
