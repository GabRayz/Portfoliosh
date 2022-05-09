package fr.gabray.portfoliosh.command.sql.statement;

import java.util.ArrayList;
import java.util.List;

public class SelectBuilder implements StatementBuilder {
    private final List<String> columns = new ArrayList<>();

    public SelectBuilder column(String name)
    {
        columns.add(name);
        return this;
    }

    public FromBuilder from(String tableName)
    {
        return new FromBuilder(this, tableName);
    }
}
