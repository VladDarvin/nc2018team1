package com.nc.airport.backend.eav.annotations.attribute.value;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Used as a marker to indicate that attribute's value is of a simple type (not a date, or list, or reference)
 *
 * @see DateField
 * @see ReferenceField
 * @see ListField
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface ValueField {
    String ID();
}
