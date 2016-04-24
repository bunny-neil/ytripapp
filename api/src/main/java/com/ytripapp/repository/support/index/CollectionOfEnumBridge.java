package com.ytripapp.repository.support.index;

import org.apache.lucene.document.Document;
import org.hibernate.search.bridge.FieldBridge;
import org.hibernate.search.bridge.LuceneOptions;

import java.util.Collection;

public class CollectionOfEnumBridge implements FieldBridge {

    @Override
    public void set(String name, Object object, Document document, LuceneOptions luceneOptions) {
        if (object != null && object instanceof Collection<?>) {
            Collection<?> items = (Collection<?>) object;
            for (Object item : items) {
                if (item != null && item.getClass().isEnum()) {
                    Enum<?> e = (Enum<?>) item;
                    luceneOptions.addFieldToDocument(name, e.name(), document);
                }
            }
        }
    }

}
