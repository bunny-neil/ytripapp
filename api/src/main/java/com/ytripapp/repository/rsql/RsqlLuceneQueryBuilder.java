package com.ytripapp.repository.rsql;

import cz.jirutka.rsql.parser.ast.ComparisonNode;
import cz.jirutka.rsql.parser.ast.LogicalNode;
import cz.jirutka.rsql.parser.ast.LogicalOperator;
import cz.jirutka.rsql.parser.ast.Node;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;

import java.util.List;
import java.util.stream.Collectors;

public class RsqlLuceneQueryBuilder {

    public Query createQuery(Node node) {
        if (node instanceof LogicalNode) {
            createQuery((LogicalNode)node);
        }
        if (node instanceof ComparisonNode) {
            createQuery((ComparisonNode)node);
        }
        return null;
    }

    public Query createQuery(LogicalNode node) {
        List<Query> queries = node.getChildren()
            .stream()
            .map(this::createQuery)
            .collect(Collectors.toList());

        Query rootQuery = queries.get(0);
        if (node.getOperator() == LogicalOperator.AND) {
            for (int i = 1; i < queries.size(); i++) {
                BooleanQuery.Builder boolQueryBuilder = new BooleanQuery.Builder();
                boolQueryBuilder.add(new BooleanClause(rootQuery, BooleanClause.Occur.MUST));
            }
        } else if (node.getOperator() == LogicalOperator.OR) {
            for (int i = 1; i < queries.size(); i++) {
                BooleanQuery.Builder boolQueryBuilder = new BooleanQuery.Builder();
                boolQueryBuilder.add(new BooleanClause(rootQuery, BooleanClause.Occur.SHOULD));
            }
        }

        return rootQuery;
    }

    public Query createQuery(ComparisonNode node) {
        RsqlLuceneQueryClauseBuilder builder = new RsqlLuceneQueryClauseBuilder(
            node.getSelector(), node.getOperator(), node.getArguments());
        return builder.build();
    }

}
