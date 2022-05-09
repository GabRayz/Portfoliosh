package fr.gabray.portfoliosh.command.sql;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class Row {

    private final Map<Column, DbData> data;

    public Row(final Map<Column, DbData> data)
    {
        this.data = data;
    }

    public Map<Column, DbData> getData()
    {
        return data;
    }

    public DbData get(Column column)
    {
        return data.get(column);
    }

    public Map<Column, DbData> get(Collection<Column> columns)
    {
        return data.entrySet().stream()
                   .filter(entry -> columns.contains(entry.getKey()))
                   .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}

