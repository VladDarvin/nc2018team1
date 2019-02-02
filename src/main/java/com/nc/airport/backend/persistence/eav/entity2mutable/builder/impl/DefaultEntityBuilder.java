package com.nc.airport.backend.persistence.eav.entity2mutable.builder.impl;

import com.nc.airport.backend.model.BaseEntity;
import com.nc.airport.backend.persistence.eav.Mutable;
import com.nc.airport.backend.persistence.eav.annotations.attribute.value.DateField;
import com.nc.airport.backend.persistence.eav.annotations.attribute.value.ListField;
import com.nc.airport.backend.persistence.eav.annotations.attribute.value.ReferenceField;
import com.nc.airport.backend.persistence.eav.annotations.attribute.value.ValueField;
import com.nc.airport.backend.persistence.eav.annotations.enums.ListValue;
import com.nc.airport.backend.persistence.eav.entity2mutable.builder.EntityBuilder;
import com.nc.airport.backend.persistence.eav.entity2mutable.util.ReflectionHelper;
import com.nc.airport.backend.persistence.eav.exceptions.InvalidAnnotatedClassException;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Map;

/**
 *  TODO javadoc
 */
@Log4j2
@Component
public class DefaultEntityBuilder implements EntityBuilder {
    private static final Marker DATA_LOSS = MarkerManager.getMarker("DATA_LOSS");

    @Override
    public <T extends BaseEntity> T build(Class<T> clazz, Mutable mutable) {
        T entity = newEntity(clazz);
        if (entity == null) {
            return null;
        }

        entity.setObjectId(mutable.getObjectId());
        entity.setParentId(mutable.getParentId());
        entity.setObjectName(mutable.getObjectName());
        entity.setObjectDescription(mutable.getObjectDescription());
        fillValueFields(entity, mutable);
        fillDateFields(entity, mutable);
        fillListFields(entity, mutable);
        fillReferenceFields(entity, mutable);

        return entity;
    }

    // TODO: 23.11.2018 refactor
    private <T extends BaseEntity> void fillReferenceFields(T entity, Mutable mutable) {
        Map<BigInteger, BigInteger> references = mutable.getReferences();
        if (references == null) {
            return;
        }

        Class<? extends BaseEntity> entityClass = entity.getClass();

        for (Map.Entry<BigInteger, BigInteger> idToRef : references.entrySet()) {
//            OBJREFERENCE.ATTR_ID - @ReferenceField(ID = "123")
            BigInteger id = idToRef.getKey();

            BigInteger referenceId = idToRef.getValue();

            Field entityField = ReflectionHelper.getFieldByAnnotationId(entityClass, ReferenceField.class, id);
            if (entityField != null) {
                ReflectionHelper.setFieldValue(entity, entityField, referenceId);
            } else {
                String message = getMessageNoFieldAnnotated(idToRef.getKey(), ReferenceField.class);
                logAndThrowDataLossEx(message, new InvalidAnnotatedClassException(message, entityClass));
            }
        }
    }


    // FIXME: 23.11.2018 implement

    private <T extends BaseEntity> void fillListFields(T entity, Mutable mutable) {
        Map<BigInteger, BigInteger> listValues = mutable.getListValues();
        if (listValues == null) {
            return;
        }

        Class<? extends BaseEntity> entityClass = entity.getClass();

        for (Map.Entry<BigInteger, BigInteger> idToEnumId : listValues.entrySet()) {
//            LISTS.ATTR_ID - @ListField(ID = "123")
            BigInteger id = idToEnumId.getKey();
//            LISTS.LIST_VALUE_ID - corresponding id is inside of @ListValue(ID = "123") annotation
            BigInteger enumId = idToEnumId.getValue();

            Field entityField = ReflectionHelper.getFieldByAnnotationId(entityClass, ListField.class, id);
            if (entityField != null) {
//                entityField.setAccessible(true);
                Class<Enum> enumClass = (Class<Enum>) entityField.getType();

                Object enumValue = null;
                for (Field enumField : enumClass.getFields()) {
                    ListValue enumAnnotation = enumField.getAnnotation(ListValue.class);
                    if (enumAnnotation != null) {
                        if (enumId.equals(new BigInteger(enumAnnotation.ID()))) {
                            try {
                                enumValue = enumField.get(null);
                            } catch (IllegalAccessException e) {
                                String msg = "Can't access enum field of " + enumClass;
                                RuntimeException exception = new InvalidAnnotatedClassException(msg, enumClass, e);
                                log.error(msg);
                                throw exception;
                            }
                        }
                    }
                }
                ReflectionHelper.setFieldValue(entity, entityField, enumValue);
            } else {
                String message = getMessageNoFieldAnnotated(idToEnumId.getKey(), ListValue.class);
                logAndThrowDataLossEx(message, new InvalidAnnotatedClassException(message, entityClass));
            }
        }
    }

    // TODO: 23.11.2018 refactor
    private <T extends BaseEntity> void fillDateFields(T entity, Mutable mutable) {
        Map<BigInteger, LocalDateTime> dateValues = mutable.getDateValues();
        if (dateValues == null) {
            return;
        }

        Class<? extends BaseEntity> entityClass = entity.getClass();

        for (Map.Entry<BigInteger, LocalDateTime> idToDate : dateValues.entrySet()) {
            BigInteger id = idToDate.getKey();
            LocalDateTime date = idToDate.getValue();

            Field entityField = ReflectionHelper.getFieldByAnnotationId(entityClass, DateField.class, id);
            if (entityField != null) {
                ReflectionHelper.setFieldValue(entity, entityField, date);
            } else {
                String message = getMessageNoFieldAnnotated(idToDate.getKey(), DateField.class);
                logAndThrowDataLossEx(message, new InvalidAnnotatedClassException(message, entityClass));
            }
        }
    }

    // TODO: 23.11.2018 refactor
    private <T extends BaseEntity> void fillValueFields(T entity, Mutable mutable) {
        Map<BigInteger, String> mutableValues = mutable.getValues();
        if (mutableValues == null) {
            return;
        }

        Class<? extends BaseEntity> entityClass = entity.getClass();

        for (Map.Entry<BigInteger, String> idToVal : mutableValues.entrySet()) {
            Field entityField = ReflectionHelper.getFieldByAnnotationId(
                    entityClass, ValueField.class, idToVal.getKey());
            if (entityField != null) {
                Class fieldClass = entityField.getType();
                Object injectableValue = newStringConstructorInstance(fieldClass, idToVal.getValue());
                ReflectionHelper.setFieldValue(entity, entityField, injectableValue);
            } else {
                String message = getMessageNoFieldAnnotated(idToVal.getKey(), ValueField.class);
                logAndThrowDataLossEx(message, new InvalidAnnotatedClassException(message, entityClass));
            }
        }
    }

    private Object newStringConstructorInstance(Class classToCreate, String initArg) {
        Object newInstance = null;
        if(initArg == null) {
            initArg = "";
        }

        try {
            Constructor<?> constructor = classToCreate.getConstructor(String.class);
            try {
                newInstance = constructor.newInstance(initArg);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                String message = "Cannot create an instance of " + classToCreate + "; argument: " + initArg;
                logAndThrowDataLossEx(message, e);
            }
        } catch (NoSuchMethodException e) {
            String message = "No constructor " + classToCreate.getName() +"(String)";
            logAndThrowDataLossEx(message, e);
        }
        return newInstance;
    }

    private <T extends BaseEntity> T newEntity(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            String message = "Cannot create new instance of class " + clazz.getName();
            logAndThrowDataLossEx(message, e);
        }
        return null;
    }

    private String getMessageNoFieldAnnotated(BigInteger id, Class<? extends Annotation> annotationClass) {
        return "No field annotated with @" + annotationClass.getSimpleName() + " with id " + id + ". " +
                "Therefore it is skipped.";
    }

    private void logAndThrowDataLossEx(String message, Throwable e) throws RuntimeException {
        RuntimeException exception = new RuntimeException(message, e);
        log.error(DATA_LOSS, message, exception);
        throw exception;
    }
}
