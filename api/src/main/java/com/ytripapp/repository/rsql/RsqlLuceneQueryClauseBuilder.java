package com.ytripapp.repository.rsql;

import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import cz.jirutka.rsql.parser.ast.RSQLOperators;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.WildcardQuery;

import java.util.List;

public class RsqlLuceneQueryClauseBuilder {

    private Analyzer defaultAnalyzer = new SmartChineseAnalyzer();
    private String property;
    private ComparisonOperator operator;
    private List<String> arguments;

    public RsqlLuceneQueryClauseBuilder(String property, ComparisonOperator operator, List<String> arguments) {
        this.property = property;
        this.operator = operator;
        this.arguments = arguments;
    }

    public Query build() {
        if (operator.equals(RSQLOperators.EQUAL)) {
            return buildEqualQuery();
        }

        if (operator.equals(RSQLOperators.NOT_EQUAL)) {
            return buildNotEqualQuery();
        }

        if (operator.equals(RSQLOperators.IN)) {
            return buildInQuery();
        }

        if (operator.equals(RsqlOperatorsExtension.LIKE)) {
            return buildLikeQuery();
        }

        return null;
    }

    /**
     * Exact match search, not tokenized
     * Example:
     *      rsql: emailAddress==annie.wu@ybagapp.com
     */
    private Query buildEqualQuery() {
        PhraseQuery.Builder phraseBuilder = new PhraseQuery.Builder();
        phraseBuilder.add(new Term(property, arguments.get(0)));
        BooleanQuery.Builder filterBuilder = new BooleanQuery.Builder();
        filterBuilder.add(phraseBuilder.build(), BooleanClause.Occur.FILTER);
        return filterBuilder.build();
    }

    /**
     * Exact match search, not tokenized
     * Example:
     *      rsql: emailAddress!=annie.wu@ybagapp.com
     */
    private Query buildNotEqualQuery() {
        WildcardQuery allValuesQuery = new WildcardQuery(new Term(property, String.valueOf(WildcardQuery.WILDCARD_STRING)));
        TermQuery exclusionQuery = new TermQuery(new Term(property, arguments.get(0)));

        BooleanQuery.Builder boolBuilder = new BooleanQuery.Builder();
        boolBuilder.add(allValuesQuery, BooleanClause.Occur.SHOULD);
        boolBuilder.add(exclusionQuery, BooleanClause.Occur.MUST_NOT);

        BooleanQuery.Builder filterBuilder = new BooleanQuery.Builder();
        filterBuilder.add(boolBuilder.build(), BooleanClause.Occur.FILTER);

        return filterBuilder.build();
    }

    /**
     * Range search, not tokenized
     * Example:
     *      rsql: firstName=in=(annie,abc)
     */
    private Query buildInQuery() {
        BooleanQuery.Builder boolBuilder = new BooleanQuery.Builder();
        arguments.forEach(arg -> boolBuilder.add(new BooleanClause(new TermQuery(new Term(property, arg)), BooleanClause.Occur.SHOULD)));
        return boolBuilder.build();
    }

    /**
     * Fuzzy search, arguements tokenzied by analyzer
     * Example:
     *      rsql: emailAddress=like=(annie,hong)
     */
    private Query buildLikeQuery() {
        try {
            QueryParser parser = new QueryParser(property, defaultAnalyzer);
            parser.setAllowLeadingWildcard(true);
            parser.setLowercaseExpandedTerms(true);
            BooleanQuery.Builder boolBuilder = new BooleanQuery.Builder();
            for(String arg : arguments) {
                boolBuilder.add(parser.parse("*" + arg + "*"), BooleanClause.Occur.SHOULD);
            }
            return boolBuilder.build();
        }
        catch (ParseException ex) {
            // ignore unknown field
        }
        return new MatchAllDocsQuery();
    }

}
