package fr.gabray.portfoliosh.command.sql;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public final class Table {

    private final String name;
    private final Map<String, Column> columns;

    @Setter
    private Column primaryKey;

    private final Map<Object, Row> rows = new HashMap<>();
    private long idIncrement = 1;

    public Table(final String name)
    {
        this.name = name;
        columns = new LinkedHashMap<>();
    }

    public Table(final String name, final Map<String, Column> columns)
    {
        this.name = name;
        this.columns = columns;
    }

    public void addColumn(Column column, boolean primaryKey)
    {
        if (!rows.isEmpty())
            throw new IllegalStateException("Cannot add column on a table with data");
        if (primaryKey && this.primaryKey != null)
            throw new IllegalStateException("Table already has a primary key");

        columns.put(column.name(), column);
        if (primaryKey)
            this.primaryKey = column;
    }

    public void insert(Map<String, Object> row)
    {
        Map<Column, Object> data = new HashMap<>();
        columns.forEach((name, col) -> data.put(col, row.get(name)));
        data.put(primaryKey, idIncrement);
        rows.put(idIncrement++, new Row(data));
    }
}

