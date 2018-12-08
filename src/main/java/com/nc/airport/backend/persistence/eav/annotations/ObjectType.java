package com.nc.airport.backend.persistence.eav.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Specifies that the class is ready to be serialized to the eav database or deserialized from it
 *
 * <pre>
 *     Example:
 *     &#064;ObjectType(ID = "1")
 *     class Airplane extends Aircraft {
 *         &#064;ValueField(ID = "1")
 *         String name;
 *
 *         &#064;ValueField(ID = "2")
 *         Integer length;
 *
 *         &#064;DateField(ID = "3")
 *         LocalDate constructed;
 *
 *         &#064;ReferenceField(ID = "4")
 *         List<Passenger> passengers;
 *
 *         &#064;ValueField(ID = "5")
 *         List<Integer> numbers;
 *
 *         &#064;ReferenceField(ID = "6")
 *         BigInt pilotRef;
 *
 *         &#064;ListField(ID = "7")
 *         Color color;
 *
 *         &#064;ObjectType(ID = "2")
 *         enum Color {
 *              RED(7),
 *              GREEN(8),
 *              BLUE(9)
 *
 *              private String id;
 *
 *              Color(String id) {this.id = id;}
 *         }
 *     }
 * </pre>
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface ObjectType {
    String ID();
}
