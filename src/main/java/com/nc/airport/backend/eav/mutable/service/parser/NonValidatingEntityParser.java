package com.nc.airport.backend.eav.mutable.service.parser;

import com.nc.airport.backend.eav.annotations.ObjectType;
import com.nc.airport.backend.eav.annotations.attribute.value.DateField;
import com.nc.airport.backend.eav.annotations.attribute.value.ListField;
import com.nc.airport.backend.eav.annotations.attribute.value.ReferenceField;
import com.nc.airport.backend.eav.annotations.attribute.value.ValueField;
import com.nc.airport.backend.eav.mutable.service.util.ReflectionHelper;
import com.nc.airport.backend.model.BaseEntity;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Does NOT check if annotated field is of correct type
 * ClassCastException is possible
 */
@Log4j2
@Component("NonValidating")
public class NonValidatingEntityParser implements EntityParser {

    @Override
    public BigInteger parseObjectTypeId(BaseEntity entity) {
        ObjectType objectTypeAnnotation = entity.getClass().getAnnotation(ObjectType.class);

        if (objectTypeAnnotation == null) {
            String message = "Class " + entity.getClass() + " is not annotated with @ObjectType";
            log.warn(message, entity);
            return null;
        }

        return new BigInteger(objectTypeAnnotation.ID());
    }


    @Override
    public Map<BigInteger, String> parseValues(BaseEntity entity) {
        Map<BigInteger, Object> parsedMap = getParsedMap(entity, ValueField.class);

        Map<BigInteger, String> idToString = new HashMap<>();
        for (Map.Entry<BigInteger, Object> pair : parsedMap.entrySet()) {
            BigInteger id = pair.getKey();
            String value = pair.getValue().toString();

            idToString.put(id, value);
        }

        return idToString;
    }

    @Override
    public Map<BigInteger, LocalDateTime> parseDateValues(BaseEntity entity) {
        Map<BigInteger, Object> parsedMap = getParsedMap(entity, DateField.class);

        Map<BigInteger, LocalDateTime> idToDate = new HashMap<>();
        for (Map.Entry<BigInteger, Object> pair : parsedMap.entrySet()) {
            BigInteger id = pair.getKey();
            LocalDateTime date = (LocalDateTime) pair.getValue();

            idToDate.put(id, date);
        }

        return idToDate;
    }

    @Override
    public Map<BigInteger, BigInteger> parseListValues(BaseEntity entity) {
        Map<BigInteger, Object> idToValue = getParsedMap(entity, ListField.class);
        if (idToValue.size() != 0) {
            // TODO: 18.11.2018 add implementation for enums
            UnsupportedOperationException exception = new UnsupportedOperationException("Enums used as POJO field");
            log.error("Enums in POJOs are unsupported", exception);
            throw exception;
        }
        return new HashMap<>();
    }

    @Override
    public Map<BigInteger, BigInteger> parseReferences(BaseEntity entity) {
        Map<BigInteger, Object> idToValue = getParsedMap(entity, ReferenceField.class);
        Map<BigInteger, BigInteger> idToReference = new HashMap<>();

        for (Map.Entry<BigInteger, Object> pair : idToValue.entrySet()) {
            BigInteger id = pair.getKey();
            BigInteger refId = (BigInteger) pair.getValue();

            idToReference.put(id, refId);
        }
        return idToReference;
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
