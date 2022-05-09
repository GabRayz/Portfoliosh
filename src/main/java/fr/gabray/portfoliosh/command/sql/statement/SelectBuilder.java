package fr.gabray.portfoliosh.command.sql.statement;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SelectBuilder implements StatementBuilder {
    private final List<String> columns = new ArrayList<>();
    @Getter
    private boolean all = false;

    public SelectBuilder column(String name)
    {
        columns.add(name);
        return this;
    }

    public FromBuilder allFrom(String tableName)
    {
        this.all = true;
        return from(tableName);
    }

    public FromBuilder from(String tableName)
    {
        return new FromBuilder(this, tableName);
    }

    public List<String> getColumns()
    {
        return Collections.unmodifiableList(columns);
    }
}
