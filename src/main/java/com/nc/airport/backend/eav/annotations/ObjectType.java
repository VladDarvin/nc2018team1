package com.nc.airport.backend.eav.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Specifies that the class is ready to be serialized to the eav database or deserialized from it
 *
 * <pre>
 *     Example:
 *     &#064;ObjectType(1)
 *     class Airplane extends Aircraft {
 *         &#064;Attribute(1)
 *         &#064;Value
 *         String name;
 *
 *         &#064;Attribute(2)
 *         &#064;Value
 *         Integer length;
 *
 *         &#064;Attribute(3)
 *         &#064;Date
 *         LocalDate constructed;
 *
 *         &#064;Attribute(4)
 *         &#064;List
 *         List<Passenger> passengers;
 *
 *         &#064;Attribute(5)
 *         &#064;Reference
 *         Pilot pilot;
 *     }
 * </pre>
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface ObjectType {
    int value();
}
