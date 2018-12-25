package com.nc.airport.backend.persistence.eav.annotations.enums;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Used as a marker for enum fields
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface ListValue {
    String ID();
}
