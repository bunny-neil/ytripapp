package com.ytripapp.repository;

import com.ytripapp.repository.rsql.RsqlLuceneQueryVistor;
import com.ytripapp.repository.rsql.RsqlOperatorsExtension;
import com.ytripapp.repository.support.Page;
import com.ytripapp.repository.support.PageRequest;
import com.ytripapp.repository.support.index.LuceneDocumentTransformer;
import com.ytripapp.repository.support.index.LuceneDocumentTransformer.ConversionResultTuple;
import com.ytripapp.repository.support.index.LuceneSortBuilder;
import com.ytripapp.repository.support.index.ParentDocumentReference;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class SearchableRepositoryBase<T, ID extends Serializable>
    extends SimpleJpaRepository<T, ID>
    implements SearchableRepository<T, ID> {

    JpaEntityInformation<T, ?> entityInformation;
    FullTextEntityManager entityManager;
    LuceneSortBuilder sortBuilder;

    public SearchableRepositoryBase(JpaEntityInformation<T, ?> ei, EntityManager em) {
        super(ei, em);
        entityInformation = ei;
        entityManager = Search.getFullTextEntityManager(em);
        sortBuilder = new LuceneSortBuilder(ei.getJavaType());
    }

    @Override
    public List<T> findAll() {
        return search(Optional.<String>empty(), PageRequest.DEFAULT).getContent();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Page<T> search(Optional<String> queryString, PageRequest pageRequest) {
        try {
            FullTextQuery query =  createFullTextQuery(createLuceneQuery(queryString), entityInformation.getJavaType());
            query.setResultTransformer(new LuceneDocumentTransformer());
            query.setFirstResult(pageRequest.getOffset()).setMaxResults(pageRequest.getSize());
            query.setSort(sortBuilder.buildLuceneSort(pageRequest.getSort()));

            List<ConversionResultTuple> entitiesTuples = query.getResultList();
            int totalCount = query.getResultSize();

            List<T> content = totalCount > pageRequest.getOffset()
                ? (List<T>) processEntityTuples(entitiesTuples) : Collections.<T> emptyList();
            totalCount = !content.isEmpty() && pageRequest.getOffset() + pageRequest.getSize() > totalCount
                ? pageRequest.getOffset() + content.size() : totalCount;
            return new Page<>(content, totalCount, pageRequest);

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private Query createLuceneQuery(Optional<String> queryString) throws ParseException {
        if (queryString.isPresent()) {
            Node rootNode = new RSQLParser(RsqlOperatorsExtension.operators).parse(queryString.get());
            return rootNode.accept(new RsqlLuceneQueryVistor());
        }
        return new MatchAllDocsQuery();
    }

    private FullTextQuery createFullTextQuery(Query luceneQuery, Class<?> clazz) {
        return entityManager.createFullTextQuery(luceneQuery, clazz)
                            .setProjection(FullTextQuery.OBJECT_CLASS, FullTextQuery.DOCUMENT);
    }

    private List<Object> processEntityTuples(List<ConversionResultTuple> tuples) throws ParseException {
        setParentsToChildren(resolveParents(tuples), tuples);
        return tuples.stream().map(ConversionResultTuple::getChild).collect(Collectors.toList());
    }

    private Map<Class<?>, List<Object>> resolveParents(List<ConversionResultTuple> tuples) throws ParseException {
        Map<Class<?>, Map<String, List<String>>> parentClassAndNameAndIds = new HashMap<>();
        for (ConversionResultTuple tuple : tuples) {
            for (ParentDocumentReference reference : tuple.getParentReferences()) {
                Map<String, List<String>> nameAndIds = parentClassAndNameAndIds.get(reference.getParentClass());
                if (nameAndIds == null) {
                    nameAndIds = new HashMap<>();
                    parentClassAndNameAndIds.put(reference.getParentClass(), nameAndIds);
                }
                List<String> ids = nameAndIds.get(reference.getParentIdName());
                if (ids == null) {
                    ids = new ArrayList<>();
                    nameAndIds.put(reference.getParentIdName(), ids);
                }
                ids.add(reference.getParentIdValue());
            }
        }

        Map<Class<?>, List<Object>> parentClassAndEntites = new HashMap<>();
        for (Class<?> parentClass : parentClassAndNameAndIds.keySet()) {
            String idName = parentClassAndNameAndIds.get(parentClass).keySet().stream().findFirst().get();
            List<String> ids = parentClassAndNameAndIds.get(parentClass).get(idName);
            List<Object> parents = searchParents(parentClass, idName, ids);
            parentClassAndEntites.put(parentClass, parents);
        }

        return parentClassAndEntites;
    }

    @SuppressWarnings("unchecked")
    private List<Object> searchParents(Class<?> parentClass, String idName,  List<String> ids) throws ParseException {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(idName).append("=in=(");
        int countOfComma = ids.size();
        for (String id : ids) {
            queryBuilder.append(id);
            if (countOfComma > 1) {
                queryBuilder.append(",");
                countOfComma--;
            }
        }
        queryBuilder.append(")");

        FullTextQuery query = createFullTextQuery(createLuceneQuery(Optional.of(queryBuilder.toString())), parentClass);
        return processEntityTuples(query.getResultList());
    }

    private void setParentsToChildren(Map<Class<?>, List<Object>> parentClassAndEntites, List<ConversionResultTuple> tuples) {
        for (ConversionResultTuple tuple : tuples) {
            BeanWrapper childWrapper = new BeanWrapperImpl(tuple.getChild());
            for (ParentDocumentReference reference : tuple.getParentReferences()) {
                List<Object> parents = parentClassAndEntites.get(reference.getParentClass());
                if (parents != null) {
                    for (Object parent : parents) {
                        BeanWrapper parentWrapper = new BeanWrapperImpl(parent);
                        Object idFromParent = parentWrapper.getPropertyValue(reference.getParentIdName());
                        if (idFromParent.toString().equals(reference.getParentIdValue())) {
                            childWrapper.setPropertyValue(reference.getParentProperty(), parent);
                            break;
                        }
                    }
                }
            }
        }
    }
}
