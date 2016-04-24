package com.ytripapp.repository.rsql;

import cz.jirutka.rsql.parser.ast.AndNode;
import cz.jirutka.rsql.parser.ast.ComparisonNode;
import cz.jirutka.rsql.parser.ast.OrNode;
import cz.jirutka.rsql.parser.ast.RSQLVisitor;
import org.apache.lucene.search.Query;

public class RsqlLuceneQueryVistor implements RSQLVisitor<Query, Void> {

    private RsqlLuceneQueryBuilder builder;

    public RsqlLuceneQueryVistor() {
        this.builder = new RsqlLuceneQueryBuilder();
    }

    @Override
    public Query visit(AndNode node, Void unused) {
        return builder.createQuery(node);
    }

    @Override
    public Query visit(OrNode node, Void unused) {
        return builder.createQuery(node);
    }

    @Override
    public Query visit(ComparisonNode node, Void unused) {
        return builder.createQuery(node);
    }

}
