package com.ytripapp.repository.support.index;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexableField;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.transform.ResultTransformer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

public class LuceneDocumentTransformer implements ResultTransformer {

    private static final long serialVersionUID = -3615122314351141514L;

    private ObjectMapper objectMapper = ParentDocumentBridge.objectMapper;

    private static Predicate<IndexableField> fieldsToIgnore() {
        return field ->
            ! field.name().equals(FullTextQuery.OBJECT_CLASS)
            && !field.name().endsWith(LuceneSortBuilder.DEFAULT_POSTFIX);
    }

    @Override
    public Object transformTuple(Object[] tuple, String[] aliases) {
        Class<?> entityClass = (Class<?>) tuple[0];
        Document document = (Document) tuple[1];
        BeanWrapper wrapper = initBeanWrapper(entityClass);

        List<ParentDocumentReference> references = new ArrayList<>();
        document.getFields()
            .stream()
            .filter(fieldsToIgnore())
            .forEach(field -> {
                if (field.stringValue().startsWith(ParentDocumentBridge.REFERENCE_PREFIX)) {
                    try {
                        references.add(
                            objectMapper.readValue(
                                stripOffReferencePrefixString(field.stringValue()),
                                ParentDocumentReference.class)
                        );
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                else {
                    wrapper.setPropertyValue(field.name(), field.stringValue());
                }
            });
        return new ConversionResultTuple(wrapper.getWrappedInstance(), references);
    }

    @Override
    public List transformList(List collection) {
        return collection;
    }

    private BeanWrapper initBeanWrapper(Class<?> entityClass) {
        BeanWrapper wrapper = new BeanWrapperImpl(BeanUtils.instantiate(entityClass));
        wrapper.setAutoGrowNestedPaths(true);
        wrapper.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public String getAsText() {
                return "" + ((Date) getValue()).getTime();
            }
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                super.setValue(new Date((Long.parseLong(text))));
            }
        });
        return wrapper;
    }

    private String stripOffReferencePrefixString(String jsonValue) {
        int pos = jsonValue.indexOf(ParentDocumentBridge.REFERENCE_PREFIX) + ParentDocumentBridge.REFERENCE_PREFIX.length();
        return jsonValue.substring(pos);
    }

    @Getter
    @Setter
    public static class ConversionResultTuple {

        Object child;
        List<ParentDocumentReference> parentReferences;

        public ConversionResultTuple(Object child, List<ParentDocumentReference> parentReferences) {
            this.child = child;
            this.parentReferences = parentReferences;
        }

    }
}
