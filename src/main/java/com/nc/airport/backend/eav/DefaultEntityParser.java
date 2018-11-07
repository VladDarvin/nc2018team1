package com.nc.airport.backend.eav;

import com.nc.airport.backend.eav.annotations.Attribute;
import com.nc.airport.backend.eav.annotations.ObjectType;
import com.nc.airport.backend.eav.annotations.attribute.value.Reference;
import com.nc.airport.backend.model.Entity;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.*;

public class DefaultEntityParser<T extends Entity> implements EntityParser<T> {
    @Override
    public BigInteger parseObjectTypeId(T entity) {
        ObjectType objectTypeAnnotation = entity.getClass().getAnnotation(ObjectType.class);
        return new BigInteger(objectTypeAnnotation.ID());
    }

    //    TODO DRY
    @Override
    public Map<BigInteger, Object> parseAttributes(T entity) {
        Map<BigInteger, Object> attributes = new HashMap<>();

        List<Field> fields = getAttributeFieldsWithValue(entity);

        for (Field attributeField : fields) {
            for (Annotation annotation : getAnnotations(attributeField)) {
                if (isAttribute(annotation)) {
                    Attribute attribute = (Attribute) annotation;
                    BigInteger attributeId = new BigInteger(attribute.ID());
                    Object attributeValue = getAttributeValue(entity, attributeField);
                    attributes.put(attributeId, attributeValue);
                }
            }
        }

        return attributes;
    }

    //    TODO DRY
    @Override
    public Map<BigInteger, BigInteger> parseReferences(T entity) {
        Map<BigInteger, BigInteger> references = new HashMap<>();

        List<Field> fields = getAttributeFieldsWithReference(entity);

        for (Field attributeField : fields) {
            for (Annotation annotation : getAnnotations(attributeField)) {
                if (isAttribute(annotation)) {
                    Attribute attribute = (Attribute) annotation;
                    BigInteger attributeId = new BigInteger(attribute.ID());
                    BigInteger attributeValue = (BigInteger) getAttributeValue(entity, attributeField);
                    references.put(attributeId, attributeValue);
                }
            }
        }

        return references;
    }

    //    TODO DRY
    private List<Field> getAttributeFieldsWithValue(T entity) {
        List<Field> attributeFieldsWithValue = new ArrayList<>();

        for (Field attributeField : getAttributeFields(entity)) {
            List<Annotation> annotations = getAnnotations(attributeField);
            if (!haveReferenceAnnotation(annotations)) {
                attributeFieldsWithValue.add(attributeField);
            }
        }

        return attributeFieldsWithValue;
    }

    //    TODO DRY
    private List<Field> getAttributeFieldsWithReference(T entity) {
        List<Field> attributeFieldsWithValue = new ArrayList<>();

        for (Field attributeField : getAttributeFields(entity)) {
            List<Annotation> annotations = getAnnotations(attributeField);
            if (haveReferenceAnnotation(annotations)) {
                attributeFieldsWithValue.add(attributeField);
            }
        }

        return attributeFieldsWithValue;
    }

    private List<Field> getAttributeFields(T entity) {
        List<Field> attributes = new ArrayList<>();
        Field[] declaredFields = entity.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            if (isAttribute(field)) {
                attributes.add(field);
            }
        }
        return attributes;
    }

    private boolean haveReferenceAnnotation(List<Annotation> annotations) {
        return annotations.contains(Reference.class);
    }

    private List<Annotation> getAnnotations(Field field) {
        return Arrays.asList(field.getDeclaredAnnotations());
    }

    private boolean isAttribute(Annotation annotation) {
        return annotation.annotationType() == Attribute.class;
    }

    private boolean isAttribute(Field field) {
        return field.isAnnotationPresent(Attribute.class);
    }

    private Object getAttributeValue(T entity, Field field) {
        try {
            field.setAccessible(true);
            return field.get(entity);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            field.setAccessible(false);
        }
        return null;
    }
}
