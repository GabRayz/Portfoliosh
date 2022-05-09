package fr.gabray.portfoliosh.command.sql.statement;

import java.util.ArrayList;
import java.util.List;

public class OrderByBuilder implements StatementBuilder {
    private final StatementBuilder parent;
    private final List<String> columns = new ArrayList<>();
    private boolean desc = false;

    public OrderByBuilder(final StatementBuilder parent, String columnName)
    {
        this.parent = parent;
        columns.add(columnName);
    }

    public OrderByBuilder thenBy(String columnName)
    {
        columns.add(columnName);
        return this;
    }

    public OrderByBuilder desc()
    {
        desc = true;
        return this;
    }

    public LimitBuilder limit(long count)
    {
        return new LimitBuilder(this, count);
    }
}
