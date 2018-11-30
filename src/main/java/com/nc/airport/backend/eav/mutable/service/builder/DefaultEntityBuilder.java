package com.nc.airport.backend.eav.mutable.service.builder;

import com.nc.airport.backend.eav.annotations.attribute.value.DateField;
import com.nc.airport.backend.eav.annotations.attribute.value.ReferenceField;
import com.nc.airport.backend.eav.annotations.attribute.value.ValueField;
import com.nc.airport.backend.eav.mutable.Mutable;
import com.nc.airport.backend.eav.mutable.service.util.ReflectionHelper;
import com.nc.airport.backend.model.BaseEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Map;

/**
 *
 */
public class DefaultEntityBuilder implements EntityBuilder {
    private static Logger LOGGER = LogManager.getLogger(DefaultEntityBuilder.class);
    private static final Marker DATA_LOSS = MarkerManager.getMarker("DATA_LOSS");

    @Override
    public <T extends BaseEntity> T build(Class<T> clazz, Mutable mutable) {
        T entity = newEntity(clazz);
        if (entity == null)
            return null;

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
        Class<? extends BaseEntity> entityClass = entity.getClass();

        for (Map.Entry<BigInteger, BigInteger> pair : references.entrySet()) {
            BigInteger id = pair.getKey();
            BigInteger reference = pair.getValue();

            Field field = ReflectionHelper.getFieldByAnnotationId(entityClass, ReferenceField.class, id);
            if (field != null) {
                ReflectionHelper.setFieldValue(entity, field, reference);
            }
        }
    }


    // FIXME: 23.11.2018 implement
    private <T extends BaseEntity> void fillListFields(T entity, Mutable mutable) {
        if (mutable.getListValues().size() > 0) {
            String message = "No implementation for enums";
            logAndThrowDataLossEx(message, new UnsupportedOperationException());
        }
    }

    // TODO: 23.11.2018 refactor
    private <T extends BaseEntity> void fillDateFields(T entity, Mutable mutable) {
        Map<BigInteger, LocalDateTime> dateValues = mutable.getDateValues();
        Class<? extends BaseEntity> entityClass = entity.getClass();

        for (Map.Entry<BigInteger, LocalDateTime> pair : dateValues.entrySet()) {
            BigInteger id = pair.getKey();
            LocalDateTime date = pair.getValue();

            Field field = ReflectionHelper.getFieldByAnnotationId(entityClass, DateField.class, id);
            if (field != null) {
                ReflectionHelper.setFieldValue(entity, field, date);
            }
        }
    }

    // TODO: 23.11.2018 refactor
    private <T extends BaseEntity> void fillValueFields(T entity, Mutable mutable) {
        Map<BigInteger, String> values = mutable.getValues();
        Class<? extends BaseEntity> entityClazz = entity.getClass();

        for (Map.Entry<BigInteger, String> pair : values.entrySet()) {
            BigInteger id = pair.getKey();
            String valueAsString = pair.getValue();
            Field field = ReflectionHelper.getFieldByAnnotationId(entityClazz, ValueField.class, id);
            if (field != null) {
                Class<?> fieldType = field.getType();
                try {
                    Constructor<?> constructor = fieldType.getConstructor(String.class);
                    try {
                        Object value = constructor.newInstance(valueAsString);
                        ReflectionHelper.setFieldValue(entity, field, value);
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        String message = "Cannot create an instance of " + fieldType;
                        logAndThrowDataLossEx(message, e);
                    }
                } catch (NoSuchMethodException e) {
                    String message = "No constructor (String) for " + fieldType;
                    logAndThrowDataLossEx(message, e);
                }
            }
        }
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

    private void logAndThrowDataLossEx(String message, Throwable e) throws RuntimeException {
        RuntimeException exception = new RuntimeException(message, e);
        LOGGER.error(DATA_LOSS, message, exception);
        throw exception;
    }
}
