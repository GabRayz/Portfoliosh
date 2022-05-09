package fr.gabray.portfoliosh.command.sql.statement;

public class FromBuilder implements StatementBuilder {
    private final SelectBuilder select;
    private final String tableName;

    public FromBuilder(final SelectBuilder select, final String tableName)
    {
        this.select = select;
        this.tableName = tableName;
    }

    public OrderByBuilder orderBy(String columnName)
    {
        return new OrderByBuilder(this, columnName);
    }

    public LimitBuilder limit(long count)
    {
        return new LimitBuilder(this, count);
    }
}
