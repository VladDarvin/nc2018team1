package com.nc.airport.backend.persistence.eav.entity2mutable.parser.impl;

import com.nc.airport.backend.model.BaseEntity;
import com.nc.airport.backend.persistence.eav.annotations.ObjectType;
import com.nc.airport.backend.persistence.eav.annotations.attribute.value.DateField;
import com.nc.airport.backend.persistence.eav.annotations.attribute.value.ListField;
import com.nc.airport.backend.persistence.eav.annotations.attribute.value.ReferenceField;
import com.nc.airport.backend.persistence.eav.annotations.attribute.value.ValueField;
import com.nc.airport.backend.persistence.eav.annotations.enums.ListValue;
import com.nc.airport.backend.persistence.eav.entity2mutable.parser.EntityParser;
import com.nc.airport.backend.persistence.eav.entity2mutable.util.ReflectionHelper;
import com.nc.airport.backend.persistence.eav.exceptions.InvalidAnnotatedClassException;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Does NOT check if annotated field is of correct type
 * ClassCastException is possible
 */
@Log4j2
@Component
public class DefaultEntityParser implements EntityParser {

    @Override
    public BigInteger parseObjectTypeId(BaseEntity entity) {
        ObjectType objectTypeAnnotation = entity.getClass().getAnnotation(ObjectType.class);

        if (objectTypeAnnotation == null) {
            String message = "Class " + entity.getClass() + " is not annotated with @ObjectType";
            RuntimeException exception = new InvalidAnnotatedClassException(message, entity.getClass());
            log.warn(message, entity);
            throw exception;
        }

        return new BigInteger(objectTypeAnnotation.ID());
    }


    @Override
    public Map<BigInteger, String> parseValues(BaseEntity entity) {
        Map<BigInteger, Object> parsedMap = getParsedMap(entity, ValueField.class);

        Map<BigInteger, String> idToString = new HashMap<>();
        for (Map.Entry<BigInteger, Object> pair : parsedMap.entrySet()) {
            BigInteger id = pair.getKey();
            Object fieldValue = pair.getValue();
            if (fieldValue instanceof Collection) {
                Collection collectionFieldValue = (Collection) fieldValue;
                for (Object collectionValue : collectionFieldValue) {
                    String stringValue = collectionValue.toString();
                    idToString.put(id, stringValue);
                }
            } else {
                String stringValue = fieldValue.toString();
                idToString.put(id, stringValue);
            }
        }

        return idToString;
    }

    @Override
    public Map<BigInteger, LocalDateTime> parseDateValues(BaseEntity entity) {
        Map<BigInteger, Object> parsedMap = getParsedMap(entity, DateField.class);

        Map<BigInteger, LocalDateTime> idToDate = new HashMap<>();
        for (Map.Entry<BigInteger, Object> pair : parsedMap.entrySet()) {
            BigInteger id = pair.getKey();
            LocalDateTime date;
            Object fieldValue = pair.getValue();
            try {
                if (fieldValue instanceof Collection) {
                    Collection collectionFieldValue = (Collection) fieldValue;
                    for (Object collectionValue : collectionFieldValue) {
                        date = (LocalDateTime) collectionValue;
                        idToDate.put(id, date);
                    }
                } else {
                    date = (LocalDateTime) fieldValue;
                    idToDate.put(id, date);
                }
            } catch (ClassCastException e) {
                String msg = "Class " + entity.getClass() + " has " + fieldValue.getClass() +
                        " field's type that is annotated with @DateField. Field's type must be LocalDateTime.";
                InvalidAnnotatedClassException exception = new InvalidAnnotatedClassException(
                        msg, entity.getClass(), e);
                log.error(msg, exception);
                throw exception;
            }


        }

        return idToDate;
    }

    @Override
    public Map<BigInteger, BigInteger> parseListValues(BaseEntity entity) {
        Map<BigInteger, BigInteger> idToEnumId = new HashMap<>();


        Map<BigInteger, Object> idToValue = getParsedMap(entity, ListField.class);

        for (Map.Entry<BigInteger, Object> pair : idToValue.entrySet()) {
//                annotation id
            BigInteger id = pair.getKey();
//                annotated field's value
            Enum value = (Enum) pair.getValue();

            Field enumField;
            try {
                enumField = value.getDeclaringClass().getField(value.name());
            } catch (NoSuchFieldException e) {
                String msg = "Annotated field's value does not exist in original Enum";
                log.error(msg, e);
                throw new InvalidAnnotatedClassException(msg, value.getDeclaringClass(), e);
            }

            ListValue annotation = enumField.getAnnotation(ListValue.class);
            if (annotation == null) {
                String msg = "Parsed enum is not annotated";
                InvalidAnnotatedClassException exception = new InvalidAnnotatedClassException(msg, value.getDeclaringClass());
                log.error(msg + " {}", value.getDeclaringClass());
                throw exception;
            }
            BigInteger valueId = new BigInteger(annotation.ID());

            idToEnumId.put(id, valueId);
        }
        return idToEnumId;
    }

    @Override
    public Map<BigInteger, BigInteger> parseReferences(BaseEntity entity) {
        Map<BigInteger, Object> idToValue = getParsedMap(entity, ReferenceField.class);
        Map<BigInteger, BigInteger> idToReference = new HashMap<>();

        for (Map.Entry<BigInteger, Object> pair : idToValue.entrySet()) {
            BigInteger id = pair.getKey();
            Object value = pair.getValue();
            if (value instanceof Collection) {
                Collection collectionValues = (Collection) value;
                for (Object collectionValue : collectionValues) {
                    BigInteger refId = (BigInteger) collectionValue;
                    idToReference.put(id, refId);
                }
            } else {
                BigInteger refId = (BigInteger) pair.getValue();
                idToReference.put(id, refId);
            }
        }
        return idToReference;
    }

    @Override
    public String generateObjectName(BaseEntity entity) {
        return entity.getClass().getName() + '@' + entity.hashCode();
    }

    /**
     * Skips fields that are empty
     *
     * @param entity          object, fields of which are used to extract values from
     * @param annotationClass class of annotation that is used to annotate field
     * @return map of annotation id and value of field that was annotated
     */
    private Map<BigInteger, Object> getParsedMap(BaseEntity entity, Class<? extends Annotation> annotationClass) {
        List<Field> allFields = ReflectionHelper.getAllFields(entity.getClass());
        List<Field> valueAnnotatedFields = ReflectionHelper.getFieldsFilteredByAnnotation(allFields, annotationClass);

        Map<BigInteger, Object> idToValue = new HashMap<>();

        for (Field field : valueAnnotatedFields) {
            Annotation annotation = field.getAnnotation(annotationClass);
            BigInteger id = ReflectionHelper.getIdFromAnnotation(annotation);
            Object value = ReflectionHelper.getFieldValue(entity, field);
            if (value != null) {
                idToValue.put(id, value);
            }
        }

        return idToValue;
    }
}
