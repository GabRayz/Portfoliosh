package fr.gabray.portfoliosh.command.sql.statement;

import fr.gabray.portfoliosh.command.sql.parser.SqlOperator;
import lombok.Getter;

@Getter
public class WhereConditionLeftBuilder {
    private final WhereBuilder whereBuilder;
    private final String columnName;

    WhereConditionLeftBuilder(final WhereBuilder whereBuilder, final String columnName)
    {
        this.whereBuilder = whereBuilder;
        this.columnName = columnName;
    }

    public WhereConditionOperatorBuilder operator(SqlOperator op)
    {
        return new WhereConditionOperatorBuilder(this, op);
    }
}
