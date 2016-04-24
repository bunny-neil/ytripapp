package com.ytripapp.repository.support.index;

import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.hibernate.search.annotations.SortableField;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LuceneSortBuilder {

    public static final String DEFAULT_POSTFIX = "_sort";

    Class rootClass;

    public LuceneSortBuilder(Class rootClass) {
        this.rootClass = rootClass;
    }

    public Sort buildLuceneSort(org.springframework.data.domain.Sort springSort) {
        Sort sort = new Sort();
        if (springSort != null) {
            List<SortField> fields = new ArrayList<>();
            for (org.springframework.data.domain.Sort.Order order : springSort) {
                SortField sortField = new SortField(getSortFieldName(order.getProperty()),
                                                    getSortFieldType(order.getProperty()),
                                                    ! order.isAscending());
                if (sortField.getType().equals(SortField.Type.STRING)) {
                    sortField.setMissingValue(SortField.STRING_LAST);
                }
                fields.add(sortField);
            }
            sort.setSort(fields.toArray(new SortField[fields.size()]));
        }
        return sort;
    }

    String getSortFieldName(String property) {
        try {
            Class<?> clazz = getPropertyJavaType(property);
            String leafName = getLeafName(property);
            Field javaField = clazz.getDeclaredField(leafName);
            SortableField found = javaField.getDeclaredAnnotation(SortableField.class);
            if (found != null) {
                if ( ! StringUtils.isEmpty(found.forField())) {
                    return found.forField();
                }
            }
        } catch (NoSuchFieldException e) {
            // ignore
        }
        return property;
    }

    SortField.Type getSortFieldType(String property) {
        Class<?> clazz = getPropertyJavaType(property);
        String leafName = getLeafName(property);
        PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(clazz, leafName);
        if (pd != null) {
            Class<?> javaType = pd.getPropertyType();

            if (Long.class.isAssignableFrom(javaType)
                || Date.class.isAssignableFrom(javaType)) {
                return SortField.Type.LONG;
            }

            if (Integer.class.isAssignableFrom(javaType)) {
                return SortField.Type.INT;
            }

            if (Float.class.isAssignableFrom(javaType)) {
                return SortField.Type.FLOAT;
            }

            if (Double.class.isAssignableFrom(javaType)) {
                return SortField.Type.DOUBLE;
            }
        }
        return SortField.Type.STRING;
    }

    Class<?> getPropertyJavaType(String name) {
        if (isNestedPath(name)) {
            String propertyName = name.substring(0, name.indexOf("."));
            PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(rootClass, propertyName);
            if (pd != null) {
                return pd.getPropertyType();
            }
        }
        return rootClass;
    }

    String getLeafName(String name) {
        if (isNestedPath(name)) {
            return name.substring(name.indexOf(".") + 1);
        }
        return name;
    }

    boolean isNestedPath(String path) {
        return path.contains(".");
    }

}
