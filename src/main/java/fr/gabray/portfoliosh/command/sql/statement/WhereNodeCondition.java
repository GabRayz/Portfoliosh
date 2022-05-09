package fr.gabray.portfoliosh.command.sql.statement;

import fr.gabray.portfoliosh.command.sql.DbData;
import fr.gabray.portfoliosh.command.sql.parser.SqlReservedWord;

import java.util.Map;

public record WhereNodeCondition(WhereCondition left, SqlReservedWord operator, WhereCondition right) implements WhereCondition {
    @Override
    public boolean check(final Map<String, DbData> row)
    {
        if (operator == SqlReservedWord.AND)
            return left.check(row) && right.check(row);
        else if (operator == SqlReservedWord.OR)
            return left.check(row) || right.check(row);
        throw new UnsupportedOperationException();
    }
}
