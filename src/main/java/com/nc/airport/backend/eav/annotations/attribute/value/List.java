package com.nc.airport.backend.eav.annotations.attribute.value;

import com.nc.airport.backend.eav.annotations.Attribute;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Used as a marker to indicate that attribute's value is a list.
 *
 * @see Attribute
 * @see Date
 * @see Reference
 * @see Value
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface List {
}
