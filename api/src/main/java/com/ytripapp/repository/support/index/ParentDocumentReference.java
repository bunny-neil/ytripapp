package com.ytripapp.repository.support.index;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.hibernate.search.annotations.DocumentId;
import org.springframework.beans.BeanUtils;

import javax.persistence.Id;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

@Getter
public class ParentDocumentReference {

    Class<?> parentClass;
    String parentIdName;
    String parentIdValue;
    String parentProperty;

    public ParentDocumentReference(Object parentObject, String parentProperty)
        throws IllegalAccessException, InvocationTargetException {
        this.parentProperty = parentProperty;
        init(parentObject);
    }

    @JsonCreator
    public ParentDocumentReference(
        @JsonProperty("parentClass") Class<?> parentClass,
        @JsonProperty("parentIdName") String parentIdName,
        @JsonProperty("parentIdValue") String parentIdValue,
        @JsonProperty("parentProperty") String parentProperty) {
        this.parentClass = parentClass;
        this.parentIdName = parentIdName;
        this.parentIdValue = parentIdValue;
        this.parentProperty = parentProperty;
    }

    private void init(Object parentObject) throws IllegalAccessException, InvocationTargetException {
        parentClass = parentObject.getClass();
        Field idField = null;
        Class<?> clazz = parentObject.getClass();
        while (clazz != null) {
            idField = findIdField(clazz);
            if (idField != null) {
                break;
            }
            clazz = clazz.getSuperclass();
        }
        assert idField != null;
        parentIdName = idField.getName();
        parentIdValue = BeanUtils.getPropertyDescriptor(clazz, idField.getName()).getReadMethod().invoke(parentObject).toString();
    }

    private Field findIdField(Class<?> clazz) {
        Field idField = null;
        Field documentIdField = null;
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                idField = field;
            }
            if (field.isAnnotationPresent(DocumentId.class)) {
                documentIdField = field;
            }
        }
        return documentIdField != null ? documentIdField : idField;
    }

}
