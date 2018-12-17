package com.nc.airport.backend.persistence.eav.entity2mutable.util;

import com.nc.airport.backend.model.BaseEntity;
import com.nc.airport.backend.persistence.eav.annotations.ObjectType;
import com.nc.airport.backend.persistence.eav.annotations.attribute.value.DateField;
import com.nc.airport.backend.persistence.eav.annotations.attribute.value.ListField;
import com.nc.airport.backend.persistence.eav.annotations.attribute.value.ReferenceField;
import com.nc.airport.backend.persistence.eav.annotations.attribute.value.ValueField;
import lombok.extern.log4j.Log4j2;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Utility class that conceals working with Reflection
 */
@Log4j2
public final class ReflectionHelper {

    private ReflectionHelper() {}

    /**
     * Gets field list from entity
     *
     * @param entityClazz an object which fields are taken
     * @return list of fields of the entity class
     */
    public static List<Field> getAllFields(Class<? extends BaseEntity> entityClazz) {
        return Arrays.asList(entityClazz.getDeclaredFields());
    }

    /**
     * Gets field's value no matter if it is accessible or not.
     *
     * @param entity an object from which field value is obtained
     * @param field  a declared private field
     * @return value of the field, or null if
     */
    public static Object getFieldValue(BaseEntity entity, Field field) {
        try {
            if (!field.isAccessible()) {
                field.setAccessible(true);
                Object value = field.get(entity);
                field.setAccessible(false);
                return value;
            } else {
                return field.get(entity);
            }
        } catch (IllegalAccessException e) {
            log.error(e);
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Sets value of object's field even if the field is final or private
     *
     * @param entity an object which field value will be set
     * @param field  a field which will be set
     * @param value  a value of a field which will be set
     */
    public static void setFieldValue(BaseEntity entity, Field field, Object value) {
        try {
            if (field.isAccessible()) {
                field.set(entity, value);
            } else {
                field.setAccessible(true);
                field.set(entity, value);
                field.setAccessible(false);
            }
        } catch (IllegalAccessException e) {
            log.error(e);
            e.printStackTrace();
        }
    }

    /**
     * Gets field list and filters it by specific annotation
     *
     * @param fields     initial list that needs to be filtered
     * @param annotation filtering criteria
     * @return list, every field of which contains annotation that is specified
     */
    public static List<Field> getFieldsFilteredByAnnotation(List<Field> fields, Class<? extends Annotation> annotation) {
        List<Field> filteredFields = new ArrayList<>();

        for (Field field : fields) {
            if (field.isAnnotationPresent(annotation)) {
                filteredFields.add(field);
            }
        }

        return filteredFields;
    }

    /**
     * Retrieves id value that is stored inside of an Annotation.
     *
     * @param annotation only ValueField, DateField, ListField and ReferenceField annotations are allowed
     * @return id as BigInteger
     * @throws IllegalArgumentException if annotation is other than those described earlier
     * @throws IllegalArgumentException if id of annotation is invalid
     */
    public static BigInteger getIdFromAnnotation(Annotation annotation) {
        Class annotationClass = annotation.annotationType();
        String idAsString;

        if (annotationClass == ValueField.class) {
            idAsString = ((ValueField) annotation).ID();
        } else if (annotationClass == DateField.class) {
            idAsString = ((DateField) annotation).ID();
        } else if (annotationClass == ListField.class) {
            idAsString = ((ListField) annotation).ID();
        } else if (annotationClass == ReferenceField.class) {
            idAsString = ((ReferenceField) annotation).ID();
        } else if (annotationClass == ObjectType.class) {
            idAsString = ((ObjectType) annotation).ID();
        } else {
            IllegalArgumentException exception = new IllegalArgumentException("Unknown annotation class " + annotationClass.getName());
            log.error(exception);
            throw exception;
        }

        BigInteger id;
        try {
            id = new BigInteger(idAsString);
        } catch (NumberFormatException e) {
            String message = "Id of annotation is invalid \"" + idAsString + "\"";
            IllegalArgumentException exception = new IllegalArgumentException(message, e);
            log.error(message, exception);
            throw exception;
        }
        return id;
    }

    /**
     * Returns field of a class that has given id. If no field present - returns null
     *
     * @param entityClazz     class of an entity which field is searched for
     * @param annotationClazz class of an annotation which is used to annotate a field
     * @param givenId         id that is mentioned in annotation value
     * @return field of a class that has given id. If no field present - returns null
     */
    public static Field getFieldByAnnotationId(Class<? extends BaseEntity> entityClazz, Class<? extends Annotation> annotationClazz, BigInteger givenId) {
        List<Field> allFields = getAllFields(entityClazz);
        List<Field> annotatedFields = getFieldsFilteredByAnnotation(allFields, annotationClazz);
        for (Field annotatedField : annotatedFields) {
            BigInteger actualId = getIdFromAnnotation(annotatedField.getAnnotation(annotationClazz));
            if (givenId.equals(actualId)) {
                return annotatedField;
            }
        }
        log.error("{DATA LOSS} No field in " + entityClazz + " with " + annotationClazz + " annotation.");
        return null;
    }

    /**
     * Returns ID of ObjType annotation of a class
     *
     * @param entityClass annotated class
     * @return ID of ObjType annotation
     */
    public static BigInteger getObjTypeId(Class<? extends BaseEntity> entityClass) {
        Annotation objTypeAnnotation = entityClass.getAnnotation(ObjectType.class);
        return getIdFromAnnotation(objTypeAnnotation);
    }
}
