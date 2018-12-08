package com.nc.airport.backend.persistence.eav.annotations.attribute.value;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Used as a marker to indicate that attribute's value is a date
 *
 * @see ReferenceField
 * @see ValueField
 * @see ListField
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface DateField {
    String ID();
}
