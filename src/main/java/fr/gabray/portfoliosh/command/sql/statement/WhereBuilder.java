package fr.gabray.portfoliosh.command.sql.statement;

import fr.gabray.portfoliosh.command.sql.Database;
import fr.gabray.portfoliosh.command.sql.ResultSet;
import fr.gabray.portfoliosh.command.sql.parser.SqlReservedWord;
import fr.gabray.portfoliosh.command.sql.parser.Whereable;
import lombok.Getter;

public class WhereBuilder implements Orderable {
    @Getter
    private final Whereable parent;
    @Getter
    private WhereCondition condition;
    private SqlReservedWord currentOp = null;

    public WhereBuilder(final Whereable parent)
    {
        this.parent = parent;
    }

    public WhereConditionLeftBuilder column(String columnName)
    {
        return new WhereConditionLeftBuilder(this, columnName);
    }

    public WhereConditionLeftBuilder and(String columnName)
    {
        currentOp = SqlReservedWord.AND;
        return column(columnName);
    }

    public WhereConditionLeftBuilder or(String columnName)
    {
        currentOp = SqlReservedWord.OR;
        return column(columnName);
    }

    public WhereConditionLeftBuilder op(SqlReservedWord op, String columnName)
    {
        currentOp = op;
        return column(columnName);
    }

    WhereBuilder addCondition(WhereCondition condition)
    {
        if (this.condition == null)
            this.condition = condition;
        else if (currentOp != null)
        {
            this.condition = new WhereNodeCondition(this.condition, currentOp, condition);
            currentOp = null;
        }
        return this;
    }

    @Override
    public ResultSet execute(final Database database)
    {
        ResultSet resultSet = parent.execute(database);

        resultSet.data().removeIf(row -> !condition.check(row));
        return resultSet;
    }

    @Override
    public LimitBuilder limit(long count)
    {
        return new LimitBuilder(this, count);
    }

    @Override
    public OrderByBuilder orderBy(String columnName)
    {
        return new OrderByBuilder(this, columnName);
    }
}

