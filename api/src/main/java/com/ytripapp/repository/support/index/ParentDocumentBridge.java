package com.ytripapp.repository.support.index;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.lucene.document.Document;
import org.hibernate.search.bridge.FieldBridge;
import org.hibernate.search.bridge.LuceneOptions;

import java.lang.reflect.InvocationTargetException;

public class ParentDocumentBridge implements FieldBridge {

    public static final String REFERENCE_PREFIX = ParentDocumentReference.class.getName() + ":";
    public static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void set(String name, Object value, Document document, LuceneOptions luceneOptions) {
        try {
            ParentDocumentReference reference = new ParentDocumentReference(value, name);
            luceneOptions.addFieldToDocument(name, getJsonString(reference), document);
        } catch (JsonProcessingException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private String getJsonString(ParentDocumentReference reference) throws JsonProcessingException {
        return REFERENCE_PREFIX + objectMapper.writeValueAsString(reference);
    }
}
