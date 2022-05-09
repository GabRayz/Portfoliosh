package fr.gabray.portfoliosh.command.sql.parser;

import lombok.Getter;

public class OperatorSqlToken extends SqlToken {
    @Getter
    private final SqlOperator operator;

    public OperatorSqlToken(final String value, final SqlOperator operator)
    {
        super(value, SqlTokenType.OPERATOR);
        this.operator = operator;
    }
}
