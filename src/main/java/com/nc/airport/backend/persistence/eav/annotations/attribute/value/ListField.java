package com.nc.airport.backend.persistence.eav.annotations.attribute.value;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Used as a marker for enum possible values.
 *
 * @see DateField
 * @see ReferenceField
 * @see ValueField
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface ListField {
    String ID();
}
