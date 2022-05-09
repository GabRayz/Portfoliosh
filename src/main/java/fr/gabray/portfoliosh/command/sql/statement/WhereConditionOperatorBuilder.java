package fr.gabray.portfoliosh.command.sql.statement;

import fr.gabray.portfoliosh.command.sql.parser.SqlOperator;
import lombok.Getter;

@Getter
public class WhereConditionOperatorBuilder {
    private final WhereConditionLeftBuilder leftBuilder;
    private final SqlOperator operator;

    public WhereConditionOperatorBuilder(final WhereConditionLeftBuilder leftBuilder, final SqlOperator operator)
    {
        this.leftBuilder = leftBuilder;
        this.operator = operator;
    }

    public WhereBuilder column(String columnName)
    {
        WhereLeafCondition condition = new WhereLeafCondition(leftBuilder.getColumnName(), columnName, operator);
        return leftBuilder.getWhereBuilder().addCondition(condition);
    }
}
