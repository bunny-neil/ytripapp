package com.ytripapp.repository.rsql;

import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import cz.jirutka.rsql.parser.ast.RSQLOperators;

import java.util.Set;

public class RsqlOperatorsExtension {

    public static final ComparisonOperator LIKE = new ComparisonOperator("=like=", true);

    public static final Set<ComparisonOperator> operators = RSQLOperators.defaultOperators();

    static {
        operators.add(LIKE);
    }

}
