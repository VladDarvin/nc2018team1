package com.nc.airport.backend.eav.annotations.attribute.value;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Used as a marker to indicate that attribute's value is a reference.
 *
 * @see ValueField
 * @see DateField
 * @see ListField
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface ReferenceField {
    String ID();
}
