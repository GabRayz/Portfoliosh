package fr.gabray.portfoliosh.command.sql.statement;

import fr.gabray.portfoliosh.command.sql.DbData;
import fr.gabray.portfoliosh.command.sql.parser.SqlOperator;

import java.util.Map;

public record WhereLeafCondition(String left, String right, SqlOperator operator) implements WhereCondition {

    @Override
    public boolean check(final Map<String, DbData> row)
    {
        return operator.test(resolve(row, left), resolve(row, right));
    }

    private String resolve(final Map<String, DbData> row, String name)
    {
        if (row.containsKey(name))
            return row.get(name).toString();
        return name;
    }
}
